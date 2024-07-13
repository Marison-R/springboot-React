package com.amigoscode.people;

import java.util.List;
import java.util.Optional;

public interface PeopleDao {
    List<People> selectAllPeoples();
    Optional<People> selectCustomerById(Integer id);
    void insertPeople(People people);
    boolean existsPeopleWithEmail(String email);
    boolean existsPeopleWithId(Integer id);
    boolean deletePeople(Integer id);
    void editPeople(Integer id, People people);
}
