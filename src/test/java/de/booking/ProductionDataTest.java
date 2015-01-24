/**
 * 
 */
package de.booking;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.booking.model.Booking;
import de.booking.service.BookingService;

/**
 * @author jim
 *
 */
public class ProductionDataTest {

	private static ConfigurableApplicationContext context;
	
	@Test
	public void testReadRecentBookings() {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		BookingService bookingService = (BookingService) context.getBean("bookingService");
		List<Booking> myBookings = bookingService.getTopNRows(20);
		
		Assert.assertTrue(myBookings.size() == 20) ;
		
		for (Booking myBooking : myBookings) {
			Assert.assertNotNull(myBooking.getId());
		}
	}
	
	@Test
	public void testNovember2014Bookings() {
		// soll 17 sein .  History test. this test can fail if data changes.
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		BookingService bookingService = (BookingService) context.getBean("bookingService");
		List<Booking> myBookings = bookingService.getBookingsByMonthYear(11, 2014, false);
		
		Assert.assertTrue(myBookings.size() == 27) ;
		Assert.assertFalse(myBookings.size() == 26) ;
	}
	
}
