package com.example.Flightbookingservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.example.Flightbookingservice.model.BookingModel;



@Repository
public interface BookingRepository extends MongoRepository<BookingModel, String>{

	BookingModel findByPnr(String pnr);

	String deleteByPnr(String pnr);

	BookingModel findByUsername(String username);

}
