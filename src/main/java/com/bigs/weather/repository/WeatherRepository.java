package com.bigs.weather.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bigs.weather.domain.Weather;

@Repository
public interface WeatherRepository extends JpaRepository<Weather, Long>{
	List<Weather> findByCategoryAndFcstDate(String category, String fcstDate);
	List<Weather> findByFcstDateAndFcstTime(String fcstDate, String fcstTime);
	List<Weather> findByFcstDateAndFcstTimeAndCategoryNotIn(String fcstDate, String fcstTime, List<String> categories);
}
