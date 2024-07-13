package com.amigoscode.people;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository("list")
public class PeopleListDataAccess implements PeopleDao{
    private static List<People> peoples;
    static {
        People ajay=new People(1L,"ajay","ajay@gmail.com",22);
        People priya=new People(2L,"priya","priya@gmail.com",25);

        peoples=new ArrayList<>();
        peoples.add(ajay);
        peoples.add(priya);
    }
    @Override
    public List<People> selectAllPeoples() {
        return peoples;
    }

    @Override
    public Optional<People> selectCustomerById(Integer id) {
       return peoples.stream()
               .filter(c-> c.getId().equals(id))
               .findFirst();

    }

    @Override
    public void insertPeople(People people) {
        peoples.add(people);
    }

    @Override
    public boolean existsPeopleWithEmail(String email) {
        return peoples.stream().anyMatch(c-> c.getEmail().equals(email));
    }

    @Override
    public boolean existsPeopleWithId(Integer id) {
        return peoples.stream().anyMatch(c-> c.getId().equals(id));
    }

    @Override
    public boolean deletePeople(Integer id) {
        Optional<People> person = selectCustomerById(id);
        person.ifPresent(peoples::remove);
        return person.isPresent();
    }

    @Override
    public void editPeople(Integer id,People people) {
          People temp=peoples.stream().filter(c-> c.getId().equals(id)).findFirst().get();
          temp.setEmail(people.getEmail());
          temp.setName(people.getName());
          temp.setAge(people.getAge());
    }


}
