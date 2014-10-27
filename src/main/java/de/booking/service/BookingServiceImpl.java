package de.booking.service;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.booking.dao.BookingDAO;
import de.booking.model.Booking;

@Service("bookingService")
public class BookingServiceImpl implements BookingService {

	@Autowired
	BookingDAO bookingDAO;	
	
	@Autowired
	private SessionFactory sessionFactory;
	
	@Transactional
	public void persistBooking(Booking myBooking) {
		bookingDAO.persistBooking(myBooking);
	}

	@Transactional
	public Booking findBookingById(long id) {
		return bookingDAO.findBookingById(id);
	}

	@Transactional
	public void updateBooking(Booking myBooking) {
		bookingDAO.updateBooking(myBooking);
	}

	@Transactional
	public void deleteBooking(Booking myBooking) {
		bookingDAO.deleteBooking(myBooking);
	}

}
