package com.example.fujitsudemo;

import com.example.fujitsudemo.DAO.weatherDAO;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FujitsudemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(FujitsudemoApplication.class, args);
        new weatherDAO();


    }

}
