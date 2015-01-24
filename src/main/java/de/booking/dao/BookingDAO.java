package de.booking.dao;

import java.util.List;

import de.booking.model.Booking;

public interface BookingDAO {

	/**
	 * 
	 * @param myBooking
	 */
	void persistBooking(Booking myBooking);

	/**
	 * 
	 * @param id
	 * @return
	 */
	Booking findBookingById(long id);

	/**
	 * 
	 * @param myBooking
	 */
	void updateBooking(Booking myBooking);

	/**
	 * 
	 * @param myBooking
	 */
	void deleteBooking(Booking myBooking);

	/**
	 * 
	 * @param myBooking
	 */
	void insertNewBookingCalcTotal(Booking myBooking);

	/**
	 * 
	 * @param numRows
	 * @return
	 */
	List<Booking> getTopNRows(int numRows);

	/**
	 * 
	 * @param month
	 * @param year
	 * @param getStorno
	 * @return
	 */
	List<Booking>  getBookingsByMonthYear(int month, int year, boolean getStorno);
	

}
