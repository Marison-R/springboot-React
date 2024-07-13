package com.amigoscode.people;

import com.amigoscode.exception.DuplicateResourceFound;
import com.amigoscode.exception.NoChangesFound;
import com.amigoscode.exception.ResourceNotFound;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {
    private final PeopleDao peopleDao;

    public PeopleService(@Qualifier("jdbc") PeopleDao peopleDao) {
        this.peopleDao = peopleDao;
    }

    public List<People> getAllCustomers(){
        return peopleDao.selectAllPeoples();
    }

    public People getCustomerById(Integer id){
        return peopleDao.selectCustomerById(id)
                .orElseThrow(
                        ()->new ResourceNotFound("people with id "+id+" not found")
                );
    }

    public void insertPeople(PeopleRegistration peopleRegistration){
        boolean emailExists=peopleDao.existsPeopleWithEmail(peopleRegistration.email());
        if(emailExists){
            throw new DuplicateResourceFound("email already exists");
        }
        peopleDao.insertPeople(new People(peopleRegistration.name(),peopleRegistration.email(), peopleRegistration.age()));
    }
    public void deletePeople(Integer id){
        if(!peopleDao.deletePeople(id)){
            throw new ResourceNotFound("people with id "+id+" not found");
        }
    }
    public void editPeople(Integer id,PeopleRegistration peopleRegistration){

        Optional<People> temp=peopleDao.selectCustomerById(id);
        if(temp.isEmpty()){
            throw new ResourceNotFound("people with id "+id+" not found");
        }
        boolean changes=false;
        if(peopleRegistration.getName()!=null && !peopleRegistration.name().equals(temp.get().getName())){
            temp.get().setName(peopleRegistration.name());
            changes=true;
        }
        if(peopleRegistration.age()!=null && !peopleRegistration.age().equals(temp.get().getAge())){
            temp.get().setAge(peopleRegistration.age());
            changes=true;
        }
        if(peopleRegistration.email()!=null && !peopleRegistration.email().equals(temp.get().getEmail())){
            if(peopleDao.existsPeopleWithEmail(peopleRegistration.email())){
                throw new DuplicateResourceFound("email already exists");
            }
            temp.get().setEmail(peopleRegistration.email());
            changes=true;
        }
        if(changes){
            peopleDao.editPeople(id,temp.get());

        }else{
            throw new NoChangesFound("no changes found");
        }
    }
}
