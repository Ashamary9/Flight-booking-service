package com.example.Flightbookingservice.externalclass;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvailableFlight {
	 private String flightNo;
	 private String flightName;
	 private String flightFrom;
	 private String flightTo;
	 private int fare;
	 private int seats;
	 private String time;
	 private String date;
	  

}
