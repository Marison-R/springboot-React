package com.amigoscode.people;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class PeopleRowMapping implements RowMapper {
    @Override
    public People mapRow(ResultSet rs, int rowNum) throws SQLException {
        People p = new People(rs.getLong("id"),rs.getString("name"),rs.getString("email"),rs.getInt("age"));
        return p;
    }
}
