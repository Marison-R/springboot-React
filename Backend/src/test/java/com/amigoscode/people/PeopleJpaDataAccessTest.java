package com.amigoscode.people;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class PeopleJpaDataAccessTest {
    @Mock
    private PeopleRepository peopleRepository;
    private PeopleJpaDataAccess underTest;
    private AutoCloseable autoCloseable;
    private Faker FAKER = new Faker();
    @BeforeEach
    void setUp() {
        autoCloseable= MockitoAnnotations.openMocks(this);
        underTest=new PeopleJpaDataAccess(peopleRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllPeoples() {
        underTest.selectAllPeoples();
        verify(peopleRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        underTest.selectCustomerById(1);
        verify(peopleRepository).findById(1);
    }

    @Test
    void insertPeople() {

        People people=new People(FAKER.name().firstName(),FAKER.internet().safeEmailAddress()+'-'+ UUID.randomUUID(),20);
        underTest.insertPeople(people);
        verify(peopleRepository).save(people);
    }

    @Test
    void existsPeopleWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + '-' + UUID.randomUUID();
        People people=new People(FAKER.name().firstName(), email,20);
        underTest.insertPeople(people);
        underTest.existsPeopleWithEmail(email);
        verify(peopleRepository).existsPeopleByEmail(email);
    }

    @Test
    void existsPeopleWithId() {

        underTest.existsPeopleWithId(1);
        verify(peopleRepository).existsPeopleById(1);
    }

    @Test
    void deletePeople() {
        boolean flag=underTest.deletePeople(1);
        verify(peopleRepository).existsById(1);
        if(flag){
        verify(peopleRepository).deleteById(1);
        }
    }

    @Test
    void editPeople() {
        String email = FAKER.internet().safeEmailAddress() + '-' + UUID.randomUUID();
        People people=new People(FAKER.name().firstName(), email,20);
        underTest.editPeople(1,people);
        verify(peopleRepository).save(people);
    }
}