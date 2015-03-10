package de.booking.service;

import java.util.List;

import de.booking.model.Booking;

public interface BookingService {

	void persistBooking(Booking myBooking);

	Booking findBookingById(long id);

	void updateBooking(Booking myBooking);

	void deleteBooking(Booking myBooking);

	void insertNewBookingCalcTotal(Booking myBooking);

	List<Booking> getTopNRows(int numRows);

	List<Booking>  getBookingsByMonthYear(int month, int year, boolean getStorno);

	/**
	 * gets all the bookings that are storno/cancelled.
	 * 
	 * @return List<Booking>
	 */
	List<Booking> getBookingsWithStorno();

}
