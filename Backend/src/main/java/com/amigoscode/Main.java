package com.amigoscode;

import com.amigoscode.people.People;
import com.amigoscode.people.PeopleRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import java.util.*;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    CommandLineRunner runner(PeopleRepository peopleRepository) {
        return args -> {
            Faker faker = new Faker();
            List<People> peopleList = new ArrayList<>();

            for (int i = 0; i < 2; i++) {
                String name = faker.name().fullName();
                String email = faker.internet().emailAddress();
                int age = faker.number().numberBetween(18, 60);

                People person = new People(name, email, age);
                peopleList.add(person);
            }

          //  peopleRepository.saveAll(peopleList);
        };
    }

}




