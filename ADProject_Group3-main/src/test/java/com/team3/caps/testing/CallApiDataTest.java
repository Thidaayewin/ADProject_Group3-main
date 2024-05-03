package com.team3.caps.testing;

import com.alibaba.fastjson.JSONObject;
import com.team3.weather.repository.RainfallRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest
class CallApiDataTest {
//
//	@Autowired
//	private RainfallRepository rainfallRepository;
//
//	@Test
//	void contextLoads() {
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime startTime = LocalDateTime.parse("2017-02-17" + " 00:00:00", formatter);
//		LocalDateTime endTime = LocalDateTime.parse("2017-06-12" + " 00:00:00", formatter);
//        //LocalDateTime endTime = LocalDateTime.parse("2017-06-12" + " 23:59:59", formatter);
//
//		//System.out.println(JSONObject.toJSONString(rainfallRepository.findRainfallInfo(startTime,endTime,"%Y-%m","2")));
//	}
}
