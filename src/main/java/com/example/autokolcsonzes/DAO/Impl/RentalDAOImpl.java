package com.example.autokolcsonzes.DAO.Impl;

import com.example.autokolcsonzes.DAO.RentalDAO;
import com.example.autokolcsonzes.Model.Rental;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class RentalDAOImpl implements RentalDAO {

    private final JdbcTemplate jdbcTemplate;

    public RentalDAOImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    @Override
    public boolean createRental(Rental rental) throws RuntimeException {
        try {
            return jdbcTemplate.update(
                    "INSERT INTO rental(carid, start, end, name, email, address, phone) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?);",
                    rental.getCarId(),
                    rental.getStart(),
                    rental.getEnd(),
                    rental.getName(),
                    rental.getEmail(),
                    rental.getAddress(),
                    rental.getPhone()
            ) == 1;
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
        return jdbcTemplate.query("SELECT * FROM rental r WHERE ? < r.end;",new RentalMapper(),date);
    }

    @Override
    public List<Rental> listCurrentForCar(int id,String date){
        return jdbcTemplate.query("SELECT * FROM rental r WHERE ? < r.end and r.carid=?;",new RentalMapper(),date,id);
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
