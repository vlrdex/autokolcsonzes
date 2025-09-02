package com.example.autokolcsonzes.DAO.Impl;

import com.example.autokolcsonzes.DAO.CarDAO;
import com.example.autokolcsonzes.Model.Car;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

@Component
public class CarDAOImpl implements CarDAO{
    private final JdbcTemplate jdbcTemplate;

    public CarDAOImpl(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }

    @Override
    public List<Car> carSearchWithParam(String start, String end) {
        return jdbcTemplate.query(
                    "SELECT * FROM car c " +
                        "WHERE c.active=True And c.id NOT IN " +
                            "(SELECT r.carid FROM rental r " +
                            "WHERE ? BETWEEN r.start AND r.end " +
                            "OR ? BETWEEN r.start AND r.end);"
                        ,new CarMapper()
                        ,start,end);
    }

    @Override
    public List<Car> findAllCar() {
        return jdbcTemplate.query(
                "SELECT * FROM car;",
                new CarMapper());
    }

    @Override
    public Car findCarById(int id) {
        return jdbcTemplate.query(
                "SELECT * FROM car WHERE car.id=?;",
                new CarMapper(),id).get(0);
    }

    @Override
    public int createCar(Car car) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO car (name, price_per_day, active) VALUES (?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, car.getName());
            ps.setDouble(2, car.getPricePerDay());
            ps.setBoolean(3, car.isActive());
            return ps;
        }, keyHolder);

        // return the generated ID
        return keyHolder.getKey().intValue();
    }

    @Override
    public boolean modifyCar(Car car) {
        return jdbcTemplate.update(
                "UPDATE car SET name=?, price_per_day=?, active=? WHERE id=?;",
                car.getName(),car.getPricePerDay(),car.isActive(),car.getId())==1;
    }

    @Override
    public boolean isAvailable(int id, String start, String end) {
        return jdbcTemplate.queryForObject(
                    "SELECT c.id NOT IN " +
                            "(SELECT r.carid FROM rental r " +
                            "WHERE ? BETWEEN r.start AND r.end " +
                            "OR ? BETWEEN r.start AND r.end) " +
                            "FROM car c WHERE c.id=?;",Boolean.class,start,end,id);
    }



    public static class CarMapper implements RowMapper<Car>{

        @Override
        public Car mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Car(rs.getInt("id"),rs.getString("name"),rs.getInt("price_per_day"),rs.getBoolean("active"));
        }
    }

}
