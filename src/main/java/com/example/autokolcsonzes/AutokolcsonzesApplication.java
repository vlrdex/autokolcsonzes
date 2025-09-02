package com.example.autokolcsonzes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.sql.DataSource;

@SpringBootApplication
public class AutokolcsonzesApplication {

	private final DataSource dataSource;

	public AutokolcsonzesApplication(DataSource dataSource){
		this.dataSource=dataSource;
	}

	public static void main(String[] args) {
		SpringApplication.run(AutokolcsonzesApplication.class, args);
	}

}
