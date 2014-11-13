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
	public List<Booking> getTopNRows(int numRows) {
		Session session = sessionFactory.getCurrentSession();  
		session.beginTransaction();  
		String sSQL  = " FROM booking order by updated_time desc LIMIT 0,";
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
	public List<Booking> getBookingsByMonthYear(int month, int year,
			boolean getStorno) {
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
