package de.booking.dao;

import de.booking.model.Booking;

public interface BookingDAO {
	
	void persistBooking(Booking myBooking);
	  
	  Booking findBookingById(long id);
	  
	  void updateBooking(Booking myBooking);
	  
	  void deleteBooking(Booking myBooking);

}
