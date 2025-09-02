package com.example.autokolcsonzes.Config;

import com.example.autokolcsonzes.DAO.CarDAO;
import com.example.autokolcsonzes.DAO.Impl.CarDAOImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class DataBaseConfig {

    @Bean
    public JdbcTemplate jdbcTemplate(final DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

}
