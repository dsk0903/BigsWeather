package com.bigs.weather.service;

import java.util.List;
import java.util.Map;

import com.bigs.weather.dto.WeatherDTO;

public interface WeatherService {
	void saveWeatherData(List<Map<String, Object>> weatherData);
	WeatherDTO findByCategoryAndFcstDate(String caterogy, String fcstDate);
	List<WeatherDTO> findByFcstDateAndFcstTime(String fcstDate, String fcstTime);
	List<WeatherDTO> findByFcstDateAndFcstTimeAndExcludeCategories(String fcstDate, String fcstTime, List<String> categories);
	
}
