package com.amigoscode.people;

import com.amigoscode.exception.DuplicateResourceFound;
import com.amigoscode.exception.NoChangesFound;
import com.amigoscode.exception.ResourceNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PeopleServiceTest {
    private PeopleService underTest;
    @Mock
    private PeopleDao peopleDao;

    @BeforeEach
    void setUp() {
        underTest=new PeopleService(peopleDao);
    }

    @Test
    void getAllCustomers() {
        underTest.getAllCustomers();
        verify(peopleDao).selectAllPeoples();
    }
    @Test
    void getCustomerById() {
        int id=1;
        People people=new People((long) id,"alex","alex@gmail.com",20);
        when(peopleDao.selectCustomerById(id)).thenReturn(Optional.of(people));

        People actual=underTest.getCustomerById(id);

        assertThat(actual).isEqualTo(people);
    }
    @Test
    void getCustomerByIdReturnsNull() {
        int id=1;
        when(peopleDao.selectCustomerById(id)).thenReturn(Optional.empty());

       assertThatThrownBy(()->underTest.getCustomerById(id)
       ).isInstanceOf(ResourceNotFound.class)
               .hasMessage("people with id "+id+" not found");
    }

    @Test
    void insertPeople() {
        String email="alex@gmail.com";
        when(peopleDao.existsPeopleWithEmail(email)).thenReturn(false);
        PeopleRegistration req=new PeopleRegistration("alex",email,20);
        underTest.insertPeople(req);


        ArgumentCaptor<People> captor=ArgumentCaptor.forClass(People.class);
        verify(peopleDao).insertPeople(captor.capture());
        People capturedPeople=captor.getValue();
        assertThat(capturedPeople).isNotNull();
        assertThat(capturedPeople.getEmail()).isEqualTo(email);
        assertThat(capturedPeople.getAge()).isEqualTo(20);
        assertThat(capturedPeople.getName()).isEqualTo("alex");
    }
    @Test
    void insertPeopleWithDuplicateEmail() {
        String email="alex@gmail.com";
        when(peopleDao.existsPeopleWithEmail(email)).thenReturn(true);
       assertThatThrownBy(()->{underTest.insertPeople(new PeopleRegistration("alex",email,20));
       }).isInstanceOf(DuplicateResourceFound.class).hasMessage("email already exists");
    }


    @Test
    void deletePeople_Success() {
        int id = 1;
        when(peopleDao.deletePeople(id)).thenReturn(true);
        underTest.deletePeople(id);
        verify(peopleDao).deletePeople(id);
    }
    @Test
    void deletePeople_NotFound() {
        int id = 2;
        when(peopleDao.deletePeople(id)).thenReturn(false);
        assertThatThrownBy(() -> underTest.deletePeople(id))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("people with id " + id + " not found");
        verify(peopleDao).deletePeople(id);
    }
    @Test
    void editPeople_ResourceNotFound() {
        int id = 1;
        PeopleRegistration update = new PeopleRegistration("Johnny", "johnny@example.com", 26);

        when(peopleDao.selectCustomerById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.editPeople(id, update))
                .isInstanceOf(ResourceNotFound.class)
                .hasMessage("people with id " + id + " not found");
    }

    @Test
    void editPeople_DuplicateEmail() {
        int id = 1;
        People existingPeople = new People("John", "john@example.com", 25);
        PeopleRegistration update = new PeopleRegistration("Johnny", "johnny@example.com", 26);

        when(peopleDao.selectCustomerById(id)).thenReturn(Optional.of(existingPeople));
        when(peopleDao.existsPeopleWithEmail(update.email())).thenReturn(true);

        assertThatThrownBy(() -> underTest.editPeople(id, update))
                .isInstanceOf(DuplicateResourceFound.class)
                .hasMessage("email already exists");
    }

    @Test
    void editPeople_NoChanges() {
        int id = 1;
        People existingPeople = new People("John", "john@example.com", 25);
        PeopleRegistration update = new PeopleRegistration("John", "john@example.com", 25);

        when(peopleDao.selectCustomerById(id)).thenReturn(Optional.of(existingPeople));

        assertThatThrownBy(() -> underTest.editPeople(id, update))
                .isInstanceOf(NoChangesFound.class)
                .hasMessage("no changes found");
    }

    @Test
    void editPeople_Success() {
        int id = 1;
        People existingPeople = new People("John", "john@example.com", 25);
        PeopleRegistration update = new PeopleRegistration("Johnny", "johnny@example.com", 26);

        when(peopleDao.selectCustomerById(id)).thenReturn(Optional.of(existingPeople));
        when(peopleDao.existsPeopleWithEmail(update.email())).thenReturn(false);

        underTest.editPeople(id, update);

        ArgumentCaptor<People> peopleArgumentCaptor = ArgumentCaptor.forClass(People.class);
        verify(peopleDao).editPeople(eq(id), peopleArgumentCaptor.capture());

        People updatedPeople = peopleArgumentCaptor.getValue();
        assertThat(updatedPeople.getName()).isEqualTo(update.name());
        assertThat(updatedPeople.getEmail()).isEqualTo(update.email());
        assertThat(updatedPeople.getAge()).isEqualTo(update.age());
    }
}
