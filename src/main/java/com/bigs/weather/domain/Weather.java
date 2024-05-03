package com.bigs.weather.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@ToString
public class Weather {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "base_date")
	private String baseDate;
	
	@Column(name = "base_time")
	private String baseTime;
	
	@Column(name = "fcst_date")
	private String fcstDate;
	
	@Column(name = "fcst_time")
	private String fcstTime;
	
	private String category;
	
	@Column(name = "fcst_value")
	private String fcstValue;
	
	private int nx;
	private int ny;
	
	@Builder
	public Weather(Long id, String baseDate, String baseTime, String fcstDate, String fcstTime, String category,
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

	public Weather() {
	}
	
	
}
