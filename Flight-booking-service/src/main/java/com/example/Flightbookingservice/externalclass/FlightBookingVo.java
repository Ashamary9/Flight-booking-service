package com.example.Flightbookingservice.externalclass;

import com.example.Flightbookingservice.model.BookingModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlightBookingVo {
	private AvailableFlight availableFlight;
	private BookingModel bookingModel;

}
