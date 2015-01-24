package de.booking.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.booking.dao.BookingDAO;
import de.booking.model.Booking;
import de.booking.model.Percentages;

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

	/**
	 * calculates total and then persists Booking booking2Insert
	 * 
	 */
	@Transactional
	public void insertNewBookingCalcTotal(Booking booking2Insert) {
		// get percentages
		Percentages currentPercentages = (Percentages) sessionFactory.getCurrentSession().get(Percentages.class, Long.valueOf("1"));

		// get total for this booking:
		float total = currentPercentages.getKreuzfahrt_percent() * booking2Insert.getKreuzfahrt();
		total += currentPercentages.getFlug_percent() * booking2Insert.getFlug();
		total += currentPercentages.getHotel_percent() * booking2Insert.getHotel();
		total += currentPercentages.getVersicherung_percent() * booking2Insert.getVersicherung() ;

		booking2Insert.setTotal(total);

		sessionFactory.getCurrentSession().save(booking2Insert);
	}

	/**
	 * Fetches the most recent <code>numRows<code> of Booking table.
	 * Recent is determined by updated_time.
	 * 
	 * @return List<Booking>
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Booking> getTopNRows(int numRows) {
		Session session = sessionFactory.getCurrentSession();  
		String sSQL  = " FROM Booking order by updated_time desc ";
		Query queryResult = session.createQuery(sSQL);  
		queryResult.setMaxResults(numRows);
		return queryResult.list(); 
	}

	/**
	 * Gets the bookings for a given 
	 * 
	 * @param   month <code>int</code> 
	 * @param   year <code>int</code>
	 * 
     * @return  List of Booking objects
	 * 
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Booking> getBookingsByMonthYear(int month, int year,
			boolean getStorno) {
		Session session = sessionFactory.getCurrentSession();    
		String sSQL  = " FROM Booking as bb " +
				" WHERE bb.month_departure="+String.valueOf(month)+"" +
				" AND bb.year_departure="+String.valueOf(year)+" ";
		if (getStorno) {
			sSQL += " AND bb.storno=0 ";
		}
		Query queryResult = session.createQuery(sSQL);  
		return queryResult.list();
	}

	/**
	 * gets all the bookings that are storno/cancelled.
	 * 
	 * @return List<Booking>
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	public List<Booking> getBookingsWithStorno() {
		Session session = sessionFactory.getCurrentSession();    
		String sSQL  = " FROM Booking as bb " +
				" WHERE bb.storno=1 ";
		Query queryResult = session.createQuery(sSQL);  
		return queryResult.list();
	}

}
