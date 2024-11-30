package com.example.Flightbookingservice.externalservice;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.Flightbookingservice.externalclass.AvailableFlight;



@FeignClient(name = "FLIGHTSERVICE", url="http://localhost:9001/flight")
public interface FlightProxy {

	@GetMapping("viewflightbyflightNo/{flightNo}")
	public AvailableFlight getFlightByNo(@PathVariable String flightNo);
	
	@PutMapping("updateflightbyid/{flightNo}")
	public AvailableFlight updateFlight(@PathVariable String flightNo,@RequestBody AvailableFlight flight); 

}
