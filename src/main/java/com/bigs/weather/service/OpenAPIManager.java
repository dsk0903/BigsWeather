package com.bigs.weather.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.bigs.weather.util.TimeUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class OpenAPIManager {
	
	private final String BASE_URL = "https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0";
	private final String apiUri = "/getVilageFcst";
	private String serviceKey = "?ServiceKey=Msd9SMCgUMaxUd0C3EX6SWpwQ%2F0rJ3KA9iSsZKRrbUnWpEcET6ViC2H0Was73%2BN0fM9PCRFqITGzltPYPTD8lQ%3D%3D";
	private String pageNo = "&pageNo=1";
	private String numOfRows = "&numOfRows=158";
	private String dataType = "&dataType=JSON";
	private String baseDate = "&base_date=20240502";
	private String baseTime = "&base_time=0200";
	private String nx = "&nx=62";
	private String ny = "&ny=130";
	
	public void setNumOfRows(int numOfRows) {
		this.numOfRows = "&numOfRows=" + numOfRows;
	}
	public void setBaseDate(String baseDate) {
	    this.baseDate = "&base_date=" + baseDate;
	}
	public void setBaseTime(String baseTime) {
	    this.baseTime = "&base_time=" + baseTime;
	}
	
	// OpenAPI 요청 Url 생성
	public String makeUrl() {
		return BASE_URL + 
				apiUri +
				serviceKey +
				pageNo +
				numOfRows +
				dataType +
				baseDate +
				baseTime +
				nx +
				ny;
	}
	
	// OpenAPI 요청 보내서 데이터 가져오기
	public String fetchOpenAPIData(String baseDate) {
		setBaseDate(baseDate);
		
		RestTemplate restTemplate = new RestTemplate();
		String jsonString = restTemplate.getForObject(makeUrl(), String.class);

		return jsonString;
	}
	
	public String fetchOpenAPIData(String baseDate, String baseTime) {
		setBaseDate(baseDate);
		setBaseTime(baseTime);
		
		RestTemplate restTemplate = new RestTemplate();
		String jsonString = restTemplate.getForObject(makeUrl(), String.class);
		
		return jsonString;
	}
	
	// 최저, 최고 기온 값 추출
	public List<Map<String, Object>> tmnTmxExtraction(String baseDate){
		List<Map<String, Object>> processedData = new ArrayList<>();
		
		String jsonString = fetchOpenAPIData(baseDate);
	
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			Map<String, Object> jsonMap = objectMapper.readValue(jsonString, Map.class);
			Map<String, Object> response = (Map<String, Object>) jsonMap.get("response");
			Map<String, Object> body = (Map<String, Object>) response.get("body");
			Map<String, Object> items = (Map<String, Object>) body.get("items");
			
			List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");
			
			for (Map<String, Object> item : itemList) {
				String category = (String) item.get("category");
                if ("TMN".equals(category) || "TMX".equals(category)) {
                    processedData.add(item);
                    
                }
			}
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return processedData;
	}
	
	// 데이터 추출 후 같은 시간대의 값만 가공하여 리턴
	public List<Map<String, Object>> dataExtraction(String baseDate, String baseTime){
		List<Map<String, Object>> processedData = new ArrayList<>();
		
		TimeUtils timeUtils = new TimeUtils();
		String fcstTime = baseTime;
		baseTime = timeUtils.setBaseTime(baseTime);
		
		String jsonString = fetchOpenAPIData(baseDate, baseTime);
	
		ObjectMapper objectMapper = new ObjectMapper();
		
		try {
			Map<String, Object> jsonMap = objectMapper.readValue(jsonString, Map.class);
			Map<String, Object> response = (Map<String, Object>) jsonMap.get("response");
			Map<String, Object> body = (Map<String, Object>) response.get("body");
			Map<String, Object> items = (Map<String, Object>) body.get("items");
			
			List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");
			
			for (Map<String, Object> item : itemList) {
				String resultFcstTime = (String) item.get("fcstTime");
                if (fcstTime.equals(resultFcstTime)) {
                    processedData.add(item);
                }
			}
			
			log.info("processedData : " + processedData);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		return processedData;
	}
	
}
