package com.example.fujitsudemo.Repos;

import org.springframework.data.repository.CrudRepository;

public interface WeatherRepo extends CrudRepository<WeatherEntity, Integer> {
}