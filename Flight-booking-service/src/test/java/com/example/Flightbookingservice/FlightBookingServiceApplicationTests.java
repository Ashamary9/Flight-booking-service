package com.example.Flightbookingservice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import com.example.Flightbookingservice.externalclass.AvailableFlight;
import com.example.Flightbookingservice.externalservice.FlightProxy;
import com.example.Flightbookingservice.model.BookingModel;
import com.example.Flightbookingservice.repository.BookingRepository;
import com.example.Flightbookingservice.service.BookingService;
import com.example.Flightbookingservice.serviceImpl.BookingServiceImpl;


@ExtendWith(MockitoExtension.class)
class FlightBookingServiceApplicationTests {

	@Test
	void contextLoads() {
	}
	
	    @InjectMocks
	    private BookingServiceImpl bookingService;

	    @Mock
	    private BookingRepository bookingRepository;

	    @Mock
	    private FlightProxy flightProxy;

	    @BeforeEach
	    public void setup() {
	        MockitoAnnotations.openMocks(this);
	    }

	    @Test
	    public void testBookTicket() {
	        BookingModel booking = new BookingModel();
	        booking.setFlightNo("FLIGHT123");
	        booking.setNumberOfTickets(2);

	        AvailableFlight flight = new AvailableFlight();
	        flight.setSeats(10);
	        flight.setFare(100);

	        when(flightProxy.getFlightByNo(booking.getFlightNo())).thenReturn(flight);
	        when(bookingRepository.save(any(BookingModel.class))).thenReturn(booking);

	        String result = bookingService.bookTicket(booking);

	        verify(flightProxy, times(1)).updateFlight(booking.getFlightNo(), flight);
	        verify(bookingRepository, times(1)).save(booking);

	        assertEquals("Ticket Confirmed Happy Journey. Your Loved Once is Waiting For You" + booking, result);
	    }

	    @Test
	    public void testCancelTicket() {
	        String pnr = "PNR123";
	        BookingModel booking = new BookingModel();
	        booking.setPnr(pnr);
	        booking.setFlightNo("FLIGHT123");
	        booking.setNumberOfTickets(2);

	        AvailableFlight flight = new AvailableFlight();
	        flight.setSeats(8);
	        flight.setFare(100);

	        when(bookingRepository.findByPnr(pnr)).thenReturn(booking);
	        when(flightProxy.getFlightByNo(booking.getFlightNo())).thenReturn(flight);

	        String result = bookingService.cancelTicket(pnr);

	        verify(bookingRepository, times(1)).deleteByPnr(pnr);
	        verify(flightProxy, times(1)).updateFlight(booking.getFlightNo(), flight);

	        assertEquals("Successfully Cancel the Ticket", result);
	    }

	    @Test
	    public void testGetAllBookings() {
	        List<BookingModel> bookingList = List.of(new BookingModel(), new BookingModel());

	        when(bookingRepository.findAll()).thenReturn(bookingList);

	        List<BookingModel> result = bookingService.getAllBookings();

	        verify(bookingRepository, times(1)).findAll();

	        assertEquals(2, result.size());
	    }
	    
	    @Test
	    void testGetBookingByPNR() {
	        // Create mock objects
	        BookingService bookingService = mock(BookingService.class);
	        BookingModel booking = new BookingModel(); // Create a mock booking object

	        // Set up the behavior of the mock objects
	        when(bookingService.getBookingByPNR(anyString())).thenReturn(booking);

	        // Call the method under test
	        BookingModel result = bookingService.getBookingByPNR("12345"); // Provide a valid PNR

	        // Verify the behavior
	        verify(bookingService, times(1)).getBookingByPNR(anyString());
	        assertEquals(booking, result);
	    }

	    @Test
	    void testGetBookingByUsername() {
	        // Create mock objects
	        BookingService bookingService = mock(BookingService.class);
	        List<BookingModel> bookings = List.of(new BookingModel(), new BookingModel()); // Create mock bookings

	        // Set up the behavior of the mock objects
	        when(bookingService.getBookingByUsername(anyString())).thenReturn(bookings);

	        // Call the method under test
	        List<BookingModel> result = bookingService.getBookingByUsername("username");

	        // Verify the behavior
	        verify(bookingService, times(1)).getBookingByUsername(anyString());
	        assertEquals(2, result.size()); // Check the number of bookings returned
	    }


	   

	}


