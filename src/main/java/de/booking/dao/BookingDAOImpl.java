package de.booking.dao;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import de.booking.model.Booking;
import de.booking.model.Percentages;

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
	 * calculates total and then persists Booking booking2Insert
	 * 
	 */
	public void insertNewBookingCalcTotal(Booking booking2Insert) {
		// get percentages
		Percentages currentPercentages = (Percentages) sessionFactory.getCurrentSession().get(Percentages.class, 1);
		
		// get total for this booking:
		float total = currentPercentages.getKreuzfahrt_percent() * booking2Insert.getKreuzfahrt();
		total += currentPercentages.getFlug_percent() * booking2Insert.getFlug();
		total += currentPercentages.getHotel_percent() * booking2Insert.getHotel();
		total += currentPercentages.getVersicherung_percent() * booking2Insert.getVersicherung() ;
		
		booking2Insert.setTotal(total);
		
		sessionFactory.getCurrentSession().persist(booking2Insert);
		
	}

	/**
	 * Fetches the most recent <code>numRows<code> of Booking table.
	 * Recent is determined by updated_time.
	 * 
	 * @return List<Booking>
	 */
	@SuppressWarnings("unchecked")
	public List<Booking> getTopNRows(int numRows) { 
		Session session = sessionFactory.getCurrentSession();  
		session.beginTransaction();  
		String sSQL  = " FROM Booking order by updated_time desc LIMIT 0,";
		sSQL = sSQL + numRows ;
		Query queryResult = session.createQuery(sSQL);  
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
	public List<Booking> getBookingsByMonthYear(int month, int year, boolean getStorno) {
		Session session = sessionFactory.getCurrentSession();  
		session.beginTransaction();  
		String sSQL  = " FROM booking as bb " +
				" WHERE bb.month_departure="+String.valueOf(month)+"" +
				" AND bb.year_departure="+String.valueOf(year)+" ";
		if (getStorno) {
			sSQL += " AND bb.storno=0 ";
		}
		Query queryResult = session.createQuery(sSQL);  
		return queryResult.list(); 
	}

}
