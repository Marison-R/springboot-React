package com.amigoscode.people;

public record PeopleRegistration(String name,String email,Integer age) {
    public Object getName() {
        return name;
    }
};
