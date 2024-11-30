package com.example.Flightbookingservice.serviceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Flightbookingservice.exception.BookingNotFoundException;
import com.example.Flightbookingservice.externalclass.AvailableFlight;
import com.example.Flightbookingservice.externalclass.FlightBookingVo;
import com.example.Flightbookingservice.externalservice.FlightProxy;
import com.example.Flightbookingservice.model.BookingModel;
import com.example.Flightbookingservice.repository.BookingRepository;
import com.example.Flightbookingservice.service.BookingService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private FlightProxy flightProxy;

	@Override
	@CircuitBreaker(name = "bookFlightCircuitBreaker",fallbackMethod = "bookFlightFallBack")
	//@SchedulerSupport(SchedulerSupport.IO)
	public String bookTicket(BookingModel booking) {
		// TODO Auto-generated method stub
		log.info("Booking a ticket method started inside the BookingServiceImpl Class");
		String pnr = generateUniquePNR();
		// Set the PNR number for the booking
		booking.setPnr(pnr);
		// Implement booking logic, validate input, etc.
    System.out.println("*****************************************************************************");
		String flightNo = booking.getFlightNo();
		AvailableFlight flightByNo = flightProxy.getFlightByNo(flightNo);

		int numberOfTickets = booking.getNumberOfTickets();
		int seats = flightByNo.getSeats();
		int reamingSets = seats - numberOfTickets;

		flightByNo.setSeats(reamingSets);

		flightProxy.updateFlight(flightNo, flightByNo);

		int fare = flightByNo.getFare();
		int totalcost = fare * numberOfTickets;

		booking.setCost(totalcost);

		BookingModel save = bookingRepository.save(booking);
		
		//paymentproxy.doPayment(booking.getUsername(), pnr, totalcost);
		
		return "Ticket Confirmed Happy Journey. Your Loved Once is Waiting For You" + save;
		
	}
	
	public String bookFlightFallBack(BookingModel booking,Exception e) {
		return "Microservices are Down";
	}
	
	private String generateUniquePNR() {
		// Generate a random UUID and use it as a PNR number (you can customize this)
		return UUID.randomUUID().toString();
	}

	@Override
	public String cancelTicket(String pnr) {
		// TODO Auto-generated method stub
		log.info("Booking a ticket method started inside the BookingServiceImpl Class");
		BookingModel booking = bookingRepository.findByPnr(pnr);
		if (booking != null) {
			log.info("Inside the if condition of cancelTicket method");

			bookingRepository.deleteByPnr(pnr);

			String flightNo = booking.getFlightNo();
			AvailableFlight flightByNo = flightProxy.getFlightByNo(flightNo);

			int numberOfTickets = booking.getNumberOfTickets();
			int seats = flightByNo.getSeats();
			int reamingSets = seats + numberOfTickets;

			flightByNo.setSeats(reamingSets);

			flightProxy.updateFlight(flightNo, flightByNo);
			return "Successfully Cancel the Ticket";
		} else {
			log.info("Inside the else condition of cancelTicket method");
			throw new BookingNotFoundException("Pnr not found");
		}
	}

	@Override
	public List<BookingModel> getAllBookings() {
		// TODO Auto-generated method stub
		log.info("getAllBookings method started inside the BookingServiceImpl Class");
		return bookingRepository.findAll();
	}

	@Override
	public BookingModel getBookingByPNR(String pnr) {
		// TODO Auto-generated method stub
		log.info("getBookingByPNR method started inside the BookingServiceImpl Class");
		BookingModel booking = bookingRepository.findByPnr(pnr);
		if (booking != null) {
			log.info("Inside the if condition of getBookingByPNR method");
			return booking;
		} else {
			log.info("Inside the else condition of getBookingByPNR method");
			throw new BookingNotFoundException("No booking was found with the pnr: " + pnr);
		}
	}

	@Override
	public List<BookingModel> getBookingByUsername(String username) {
		// TODO Auto-generated method stub
		log.info("getBookingByUsername method started inside the BookingService class");
        
        List<BookingModel> list = new ArrayList<BookingModel>();
        List<BookingModel> all = bookingRepository.findAll();

        for(BookingModel b : all) {
        	
         String username2 = b.getUsername();
         if(username2.equals(username)) {
        	 list.add(b);
         }
        }
        
        return list;
	}

	@Override
	public FlightBookingVo getTicketDetailsWithTrainDetaisl(String pnr) {
		// TODO Auto-generated method stub
		BookingModel booking = bookingRepository.findByPnr(pnr);

		FlightBookingVo vo = new FlightBookingVo();

		if (booking != null) {
			String flightNo = booking.getFlightNo();
			AvailableFlight flightByNo = flightProxy.getFlightByNo(flightNo);
			
			String username = booking.getUsername();
			 //LoginModel userDetails = loginProxy.getbyUserName(username);

			
			
			vo.setAvailableFlight(flightByNo);
			vo.setBookingModel(booking);
			//vo.setLoginModel(userDetails);
			
			bookingRepository.save(booking);

		} else {
			log.warn("Pnr is not Found");
		}
		return vo;
	}

	}
	
	
	
	
	
	

