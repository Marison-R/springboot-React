package com.amigoscode.people;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PeopleRepository extends JpaRepository<People, Integer> {
    boolean existsPeopleByEmail(String email);
    boolean existsPeopleById(int id);
}
