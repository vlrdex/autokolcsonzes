package com.example.autokolcsonzes.DAO.Impl;

import com.example.autokolcsonzes.DAO.RentalDAO;
import com.example.autokolcsonzes.Model.Rental;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Component
public class RentalDAOImpl implements RentalDAO {

    private final JdbcTemplate jdbcTemplate;

    public RentalDAOImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    @Override
    public int createRental(Rental rental) throws RuntimeException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();

            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        "INSERT INTO rental(carid, start, end, name, email, address, phone) VALUES (?, ?, ?, ?, ?, ?, ?)",
                        Statement.RETURN_GENERATED_KEYS
                );
                ps.setInt(1, rental.getCarId());
                ps.setDate(2, java.sql.Date.valueOf(rental.getStart()));
                ps.setDate(3, java.sql.Date.valueOf(rental.getEnd()));
                ps.setString(4, rental.getName());
                ps.setString(5, rental.getEmail());
                ps.setString(6, rental.getAddress());
                ps.setString(7, rental.getPhone());
                return ps;
            }, keyHolder);

            return keyHolder.getKey().intValue(); // return the generated ID

        } catch (DataAccessException ex) {
            Throwable rootCause = ex.getRootCause();
            throw new RuntimeException(rootCause.getMessage(), ex);
        }
    }


    @Override
    public List<Rental> listAll() {
        return jdbcTemplate.query("Select * FROM rental",new RentalMapper());
    }

    @Override
    public List<Rental> listCurrent(String date){
        return jdbcTemplate.query("SELECT * FROM rental r WHERE r.end > ?;",new RentalMapper(),date);
    }

    @Override
    public List<Rental> listCurrentForCar(int id,String date){
        return jdbcTemplate.query("SELECT * FROM rental r WHERE r.end > ? and r.carid=?;",new RentalMapper(),date,id);
    }

    public class RentalMapper implements RowMapper<Rental>{

        @Override
        public Rental mapRow(ResultSet rs, int rowNum) throws SQLException {
            Rental rental=new Rental();
            rental.setId(rs.getInt("id"));
            rental.setCarId(rs.getInt("carid"));
            rental.setStart(rs.getString("start"));
            rental.setEnd(rs.getString("end"));
            rental.setName(rs.getString("name"));
            rental.setEmail(rs.getString("email"));
            rental.setAddress(rs.getString("address"));
            rental.setPhone(rs.getString("phone"));

            return rental;
        }
    }
}
