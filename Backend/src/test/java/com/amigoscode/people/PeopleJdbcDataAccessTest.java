package com.amigoscode.people;

import com.amigoscode.AbstractTestContainer;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class PeopleJdbcDataAccessTest extends AbstractTestContainer {
    private PeopleJdbcDataAccess underTest;
    private PeopleRowMapping peopleRowMapping = new PeopleRowMapping();

    @BeforeEach
    void setUp() {
        underTest = new PeopleJdbcDataAccess(getJdbcTemplate(), peopleRowMapping);
    }

    @Test
    void selectAllPeoples() {
        People people = new People(FAKER.name().firstName(), FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID(), 20);
        underTest.insertPeople(people);
        List<People> peoples = underTest.selectAllPeoples();
        assertThat(peoples).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        People people = new People(FAKER.name().firstName(), email, 20);
        underTest.insertPeople(people);
        long id = underTest.selectAllPeoples().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .map(People::getId)
                .orElseThrow();
        Optional<People> actual = underTest.selectCustomerById((int) id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(people.getEmail());
            assertThat(c.getName()).isEqualTo(people.getName());
            assertThat(c.getAge()).isEqualTo(people.getAge());
        });
    }

    @Test
    void insertPeople() {

    }

    @Test
    void existsPeopleWithEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        People people = new People(FAKER.name().firstName(), email, 20);
        underTest.insertPeople(people);
        boolean exists = underTest.existsPeopleWithEmail(email);
        assertThat(exists).isTrue();
    }

    @Test
    void existsPeopleWithId() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        People people = new People(FAKER.name().firstName(), email, 20);
        underTest.insertPeople(people);
        long id = underTest.selectAllPeoples().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .map(People::getId)
                .orElseThrow();
        boolean exists = underTest.existsPeopleWithId((int) id);
        assertThat(exists).isTrue();
    }

    @Test
    void deletePeople() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        People people = new People(FAKER.name().firstName(), email, 20);
        underTest.insertPeople(people);
        long id = underTest.selectAllPeoples().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .map(People::getId)
                .orElseThrow();
        boolean deleted = underTest.deletePeople((int) id);
        assertThat(deleted).isTrue();
        boolean exists = underTest.existsPeopleWithId((int) id);
        assertThat(exists).isFalse();
    }

    @Test
    void editPeople() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        People people = new People(FAKER.name().firstName(), email, 20);
        underTest.insertPeople(people);
        long id = underTest.selectAllPeoples().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .map(People::getId)
                .orElseThrow();
        People updatedPeople = new People(FAKER.name().firstName(), email, 25);
        underTest.editPeople((int) id, updatedPeople);
        Optional<People> actual = underTest.selectCustomerById((int) id);
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getEmail()).isEqualTo(updatedPeople.getEmail());
            assertThat(c.getName()).isEqualTo(updatedPeople.getName());
            assertThat(c.getAge()).isEqualTo(updatedPeople.getAge());
        });
    }
}
