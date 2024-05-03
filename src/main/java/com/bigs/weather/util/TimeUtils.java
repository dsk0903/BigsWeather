package com.bigs.weather.util;

public class TimeUtils {
	public static String setBaseTime(String baseTime) {
		int tmpInt = Integer.parseInt(baseTime);
		tmpInt = tmpInt / 100;
		String resultStr = "";
		if(tmpInt < 3) {
			resultStr = "2300";
		} else if(tmpInt < 6) {
			resultStr = "0200";
		} else if(tmpInt < 9) {
			resultStr = "0500";
		} else if(tmpInt < 12) {
			resultStr = "0800";
		} else if(tmpInt < 15) {
			resultStr = "1100";
		} else if(tmpInt < 18) {
			resultStr = "1400";
		} else if(tmpInt < 21) {
			resultStr = "1700";
		} else if(tmpInt < 24) {
			resultStr = "2000";
		} 
		return resultStr;
	}
	
	public static void main ( String args[]) {
		System.out.println("나와용?" + setBaseTime("0800"));
	}
}
