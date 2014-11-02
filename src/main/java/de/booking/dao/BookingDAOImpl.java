package de.booking.dao;

import java.util.Hashtable;

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

	/**
	 * calculates total and then persists object
	 * 
	 */
	public void insertNewBookingCalcTotal(Booking booking2Insert) {
		// get percentages
		Hashtable<String, Float> percentages_hashtable = getPercentages();
		
		// get total for this booking:
		float total = percentages_hashtable.get(kreuzfahrt_percent_column) * booking2Insert.getkreuzfahrt();
		total = total + percentages_hashtable.get(this.flug_percent_column) * booking2Insert.getflug();
		total = total + percentages_hashtable.get(this.hotel_percent_column) * booking2Insert.gethotel();
		total = total + percentages_hashtable.get(this.versicherung_percent_column) * booking2Insert.getversicherung();
		
		
		sessionFactory.getCurrentSession().persist(booking2Insert);
		
	}

}
