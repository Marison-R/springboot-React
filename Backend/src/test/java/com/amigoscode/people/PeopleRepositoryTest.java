package com.amigoscode.people;

import com.amigoscode.AbstractTestContainer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PeopleRepositoryTest extends AbstractTestContainer {
    @Autowired
    private PeopleRepository underTest;
    @BeforeEach
    void setUp() {
    }

    @Test
    void existsPeopleByEmail() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        People people = new People(FAKER.name().firstName(), email, 20);
        underTest.save(people);

        boolean actual = underTest.existsPeopleByEmail(email);
        assertThat(actual).isTrue();
    }

    @Test
    void existsPeopleById() {
        String email = FAKER.internet().safeEmailAddress() + "-" + UUID.randomUUID();
        People people = new People(FAKER.name().firstName(), email, 20);
        underTest.save(people);
        long id = underTest.findAll().stream()
                .filter(c -> c.getEmail().equals(email))
                .findFirst()
                .map(People::getId)
                .orElseThrow();
        boolean actual = underTest.existsPeopleById((int) id);
        assertThat(actual).isTrue();

    }
}