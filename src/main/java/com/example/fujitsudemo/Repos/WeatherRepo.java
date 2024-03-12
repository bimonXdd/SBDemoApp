package com.example.fujitsudemo.Repos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface WeatherRepo extends CrudRepository<WeatherEntity, Integer> {
}