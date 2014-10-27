package de.booking.service;

import de.booking.model.Booking;

public interface BookingService {
	
	void persistBooking(Booking myBooking);
	  
	  Booking findBookingById(long id);
	  
	  void updateBooking(Booking myBooking);
	  
	  void deleteBooking(Booking myBooking);

}
