package com.amigoscode.people;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository("jdbc")
public class PeopleJdbcDataAccess implements PeopleDao{
    private final JdbcTemplate jdbcTemplate;
    private final PeopleRowMapping peopleRowMapping;
    public PeopleJdbcDataAccess(JdbcTemplate jdbcTemplate, PeopleRowMapping peopleRowMapping) {
        this.jdbcTemplate = jdbcTemplate;
        this.peopleRowMapping = peopleRowMapping;
    }

    @Override
    public List<People> selectAllPeoples() {
        var sql="select * from people";

        return jdbcTemplate.query(sql, peopleRowMapping);
    }
    @Override
    public Optional<People> selectCustomerById(Integer id) {
        var sql = """
                SELECT * FROM people WHERE id = ?
                """;

        return jdbcTemplate.query(sql, peopleRowMapping, id).stream().findFirst();
    }

    @Override
    public void insertPeople(People people) {
            var sql= """
                    INSERT INTO people(name,email,age) VALUES (?,?,?)
                    """;
            int res=jdbcTemplate.update(sql,people.getName(),people.getEmail(),people.getAge());
        System.out.println("affected row"+res);
    }

    @Override
    public boolean existsPeopleWithEmail(String email) {
        var sql="select * from people where email = ?";
        long res=jdbcTemplate.query(sql, peopleRowMapping, email).stream().count();
        return (res!=0);
    }

    @Override
    public boolean existsPeopleWithId(Integer id) {
        var sql="select * from people where id = ?";
        long res=jdbcTemplate.query(sql, peopleRowMapping, id).stream().count();
        return (res!=0);
    }

    @Override
    public boolean deletePeople(Integer id) {
        var sql= """
                delete from people WHERE id = ?
                """;
        int res=jdbcTemplate.update(sql,id);
        System.out.println("affected row"+res);
        return res==1;
    }

    @Override
    public void editPeople(Integer id, People people) {
            var sql="update people set name = ?, email = ?, age = ? where id = ?";
            int res=jdbcTemplate.update(sql, people.getName(), people.getEmail(), people.getAge(), id);
          System.out.println("affected row"+res);

    }
}
