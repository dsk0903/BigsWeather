package com.bigs.weather.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bigs.weather.domain.Weather;
import com.bigs.weather.dto.WeatherDTO;
import com.bigs.weather.repository.WeatherRepository;

@Service
public class WeatherServiceImpl implements WeatherService{
	
	@Autowired
	WeatherRepository weatherRepository;
	
	@Override
	public void saveWeatherData(List<Map<String, Object>> weatherData) {
		for (Map<String, Object> item : weatherData) {
            Weather weather = Weather.builder()
                    .baseDate((String) item.get("baseDate"))
                    .baseTime((String) item.get("baseTime"))
                    .fcstDate((String) item.get("fcstDate"))
                    .fcstTime((String) item.get("fcstTime"))
                    .category((String) item.get("category"))
                    .fcstValue((String) item.get("fcstValue"))
                    .nx((int) item.get("nx"))
                    .ny((int) item.get("ny"))
                    .build();
            weatherRepository.save(weather);
        }
	}
	
	// 최저, 최고 기온 select 
	@Override
	public WeatherDTO findByCategoryAndFcstDate(String caterogy, String fcstDate) {
		List<Weather> weatherEntities = weatherRepository.findByCategoryAndFcstDate(caterogy, fcstDate);
	    if (!weatherEntities.isEmpty()) {
	        Weather firstWeather = weatherEntities.get(0);
	        return convertToDTO(firstWeather);
	    } else {
	        return null;
	    }
	}
	
	@Override
	public List<WeatherDTO> findByFcstDateAndFcstTimeAndExcludeCategories(String fcstDate, String fcstTime,
			List<String> categories) {
		List<Weather> weatherEntities = weatherRepository.findByFcstDateAndFcstTimeAndCategoryNotIn(fcstDate, fcstTime, categories);
        return weatherEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
	}
	
	@Override
	public List<WeatherDTO> findByFcstDateAndFcstTime(String fcstDate, String fcstTime) {
		List<Weather> weatherEntities = weatherRepository.findByFcstDateAndFcstTime(fcstDate, fcstTime);
        return weatherEntities.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
	}
	
	
    private WeatherDTO convertToDTO(Weather weather) {
        return WeatherDTO.builder()
                .id(weather.getId())
                .baseDate(weather.getBaseDate())
                .baseTime(weather.getBaseTime())
                .fcstDate(weather.getFcstDate())
                .fcstTime(weather.getFcstTime())
                .category(weather.getCategory())
                .fcstValue(weather.getFcstValue())
                .nx(weather.getNx())
                .ny(weather.getNy())
                .build();
    }
	
}
