package com.amigoscode.people;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PeopleRowMappingTest {

    @Test
    void mapRow() throws SQLException {
        PeopleRowMapping peopleRowMapping = new PeopleRowMapping();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong("id")).thenReturn(1l);
        when(resultSet.getString("name")).thenReturn("John Doe");
        when(resultSet.getInt(  "age")).thenReturn(25);
        when(resultSet.getString("email")).thenReturn("john.doe@example.com");

        People people=new People(1l,"John Doe","john.doe@example.com",25);
        People actudal=peopleRowMapping.mapRow(resultSet,1);
        assertEquals(actudal.getName(),"John Doe");
        assertEquals(actudal.getAge(),25);
        assertEquals(actudal.getEmail(),"john.doe@example.com");
        assertEquals(actudal.getId(),1);
    }
}