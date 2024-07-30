package com.example.Price.Tracker;


import org.openqa.selenium.By;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SpringBootApplication
public class PriceTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PriceTrackerApplication.class, args);
		Date date=new Date();
		Calendar cal = Calendar.getInstance();
		System.out.println(new SimpleDateFormat("MMM").format(cal.getTime()).toString()+"-"+date.getDate());

	}

}
