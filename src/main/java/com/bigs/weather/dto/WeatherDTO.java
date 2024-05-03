package com.bigs.weather.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class WeatherDTO {
	private Long id;
	private String baseDate;
	private String baseTime;
	private String fcstDate;
	private String fcstTime;
	private String category;
	private String fcstValue;
	private int nx;
	private int ny;
	
	public WeatherDTO() {
	}
	
	@Builder
	public WeatherDTO(Long id, String baseDate, String baseTime, String fcstDate, String fcstTime, String category,
			String fcstValue, int nx, int ny) {
		this.id = id;
		this.baseDate = baseDate;
		this.baseTime = baseTime;
		this.fcstDate = fcstDate;
		this.fcstTime = fcstTime;
		this.category = category;
		this.fcstValue = fcstValue;
		this.nx = nx;
		this.ny = ny;
	}
	
	
}
