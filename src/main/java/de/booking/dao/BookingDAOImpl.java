package de.booking.dao;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.booking.model.Booking;

@Repository("bookingDAO")
public class BookingDAOImpl implements BookingDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	
	public void persistBooking(Booking myBooking) {
		sessionFactory.getCurrentSession().persist(myBooking);
	}

	public Booking findBookingById(long id) {
		return (Booking) sessionFactory.getCurrentSession().get(Booking.class, id);
	}

	public void updateBooking(Booking myBooking) {
		sessionFactory.getCurrentSession().update(myBooking);
	}

	public void deleteBooking(Booking myBooking) {
		sessionFactory.getCurrentSession().delete(myBooking);
	}

}
