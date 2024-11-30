package com.example.Flightbookingservice.service;

import java.util.List;

import com.example.Flightbookingservice.externalclass.FlightBookingVo;
import com.example.Flightbookingservice.model.BookingModel;



public interface BookingService {

	 public String bookTicket(BookingModel booking);
	 public String cancelTicket(String pnr);
	 public List<BookingModel> getAllBookings();
	 public BookingModel getBookingByPNR(String pnr);
	 public List<BookingModel> getBookingByUsername(String username);
	 public FlightBookingVo getTicketDetailsWithTrainDetaisl(String pnr);
}

