package com.bigs.weather.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bigs.weather.dto.WeatherDTO;
import com.bigs.weather.service.OpenAPIManager;
import com.bigs.weather.service.WeatherService;
import com.bigs.weather.util.TimeUtils;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class WeatherController {

	@Autowired
	OpenAPIManager openAPIManager;
	@Autowired
	WeatherService weatherService;
	
	
	
	@GetMapping("/")
	public String weather() {
		
		//List<Map<String, Object>> tmnTmxList = new ArrayList<>(); 
		//tmnTmxList = openAPIManager.tmnTmxExtraction();
		//weatherService.saveWeatherData(tmnTmxList);
		
		return "weather";
	}
	
	@PostMapping("/weatherResult")
	public String weatherResult(@RequestParam String baseDate, @RequestParam String baseTime, Model model) {
		baseTime = baseTime + "00";
	    String tmn = "-1";
	    String tmx = "-1";
	    
		// 최저, 최고 기온 select
		WeatherDTO tmnDTO = weatherService.findByCategoryAndFcstDate("TMN", baseDate);
		if(tmnDTO != null) {			
			tmn = tmnDTO.getFcstValue();
		}
	    WeatherDTO tmxDTO = weatherService.findByCategoryAndFcstDate("TMX", baseDate);
		if(tmxDTO != null) {			
			tmx = tmxDTO.getFcstValue();
		}
	    
		// 최저, 최고 기온이 없을 때 insert 이후 변수에 저장
		if(tmn == "-1" || tmx == "-1") {
	    	List<Map<String, Object>> tmnTmxList = new ArrayList<>(); 
	    	tmnTmxList = openAPIManager.tmnTmxExtraction(baseDate);
	    	weatherService.saveWeatherData(tmnTmxList);
	    	
	    	WeatherDTO tmnDTO2 = weatherService.findByCategoryAndFcstDate("TMN", baseDate);
	    	tmn = tmnDTO2.getFcstValue();
	    	
	    	WeatherDTO tmxDTO2 = weatherService.findByCategoryAndFcstDate("TMX", baseDate);
	    	tmx = tmxDTO2.getFcstValue();
	    } else { // 최저, 최고 기온이 있으면 전체 데이터 조회
	    	List<WeatherDTO> findData = new ArrayList();
	    	List<String> categories = new ArrayList<>();
	    	categories.add("TMN");
	    	categories.add("TMX");
	    	
	    	// category 밸류가 최저, 최고 기온이 아닌 것들 조회
	    	findData = weatherService.findByFcstDateAndFcstTimeAndExcludeCategories(baseDate, baseTime, categories);
	    	
	    	// 찾은 데이터의 개수가 부족한 경우 insert
	    	if (findData.size() < 10) {
	    		List<Map<String, Object>> dataExtraction = new ArrayList<>();
	    		dataExtraction = openAPIManager.dataExtraction(baseDate, baseTime);
	    		weatherService.saveWeatherData(dataExtraction);
	    	}
	    	
	    	// 전체 데이터 조회
	    	List<WeatherDTO> findAllData = new ArrayList<>();
	    	findAllData = weatherService.findByFcstDateAndFcstTime(baseDate, baseTime);
	    	
	    	// 카테고리별 값 추출
	    	Map<String, String> categoryValueMap = findAllData.stream()
	                .collect(Collectors.toMap(
	                        WeatherDTO::getCategory,
	                        WeatherDTO::getFcstValue
	                		));
	    	
	    	// 추출된 값 model에 형태로 삽입
	        for (Map.Entry<String, String> entry : categoryValueMap.entrySet()) {
	        	model.addAttribute(entry.getKey(), entry.getValue());
	        }
	    }
	    model.addAttribute("TMN", tmn);
	    model.addAttribute("TMX", tmx);
 
		 String year = baseDate.substring(0, 4);
		 String month = baseDate.substring(4, 6);
		 String day = baseDate.substring(6);
		 String formattedDate = year + "년 " + month + "월 " + day + "일";
	    
	    model.addAttribute("baseDate", formattedDate);
	    model.addAttribute("baseTime", baseTime.substring(0, baseTime.length() -2 ));
	    System.out.println(model);
	    return "weatherResult";
	}
}
