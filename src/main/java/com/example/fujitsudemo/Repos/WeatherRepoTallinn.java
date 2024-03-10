package com.example.fujitsudemo.Repos;

import org.springframework.data.repository.CrudRepository;

public interface WeatherRepoTallinn extends CrudRepository<WeatherTallinn, Integer> {
}