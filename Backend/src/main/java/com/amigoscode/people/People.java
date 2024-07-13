package com.amigoscode.people;

import jakarta.persistence.*;

import java.util.Objects;
@Entity
@Table(
        name = "people",
        uniqueConstraints = {
                @UniqueConstraint(

                name = "unique_email",
                columnNames = "email"
                )
        }


)
 public class People{
    @Id
    @SequenceGenerator(
            name = "people_id_seq",
            sequenceName = "people_id_seq",
            allocationSize = 1

    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "people_id_seq"
    )
    private Long id;
    @Column(
        nullable = false
    )
    private String name;
    @Column(
            nullable = false
    )
    private String email;
    @Column(
            nullable = false
    )
    private int age;

    public People() {

    }
    public People(People people){
        this.id = people.getId();
        this.name = people.getName();
        this.email = people.getEmail();
        this.age = people.getAge();
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }


    public People(Long id, String name, String email, int age) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }
    public People(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        People people = (People) o;
        return age == people.age && Objects.equals(id, people.id) && Objects.equals(name, people.name) && Objects.equals(email, people.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, age);
    }

    @Override
    public String toString() {
        return "People{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", age=" + age +
                '}';
    }
}
