package com.amigoscode.people;

import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/v1/peoples")
public class PeopleController {
    private final PeopleService peopleService;
    private final PeopleRepository peopleRepository;

    public PeopleController(PeopleService peopleService, PeopleRepository peopleRepository) {
        this.peopleService = peopleService;
        this.peopleRepository = peopleRepository;
    }

    @GetMapping
    public List<People> getPeoples(){
        return peopleService.getAllCustomers();
    }
    @GetMapping("{id}")
    public People getPeople(
            @PathVariable("id")Integer id)
    {
        return peopleService.getCustomerById(id);
    }
    @PostMapping
    public void createPeople(
            @RequestBody PeopleRegistration people
    ){
        peopleService.insertPeople(people);
    }
    @DeleteMapping("{id}")
    public void deletePeople(@PathVariable("id") Integer id ){
        peopleService.deletePeople(id);
    }

    @PutMapping("{id}")
    public void editPeople(@PathVariable("id") Integer id, @RequestBody PeopleRegistration people){
        peopleService.editPeople(id, people);
    }
}
