package com.amigoscode.people;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository("jpa")
public class PeopleJpaDataAccess implements PeopleDao{
    private final PeopleRepository peopleRepository;

    public PeopleJpaDataAccess(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    @Override
    public List<People> selectAllPeoples() {
        return peopleRepository.findAll();
    }

    @Override
    public Optional<People> selectCustomerById(Integer id) {
        return peopleRepository.findById(id);
    }

    @Override
    public void insertPeople(People people) {
        peopleRepository.save(people);
    }

    @Override
    public boolean existsPeopleWithEmail(String email) {
        return peopleRepository.existsPeopleByEmail(email);
    }

    @Override
    public boolean existsPeopleWithId(Integer id) {
        return peopleRepository.existsPeopleById(id);
    }

    @Override
    public boolean deletePeople(Integer id) {
        if (peopleRepository.existsById(id)) {
            peopleRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public void editPeople(Integer id, People people) {
            peopleRepository.save(people);
    }
}
