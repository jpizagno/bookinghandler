package de.booking;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.booking.model.Booking;
import de.booking.service.BookingService;

public class BookingTester {
	
	private static ConfigurableApplicationContext context;

	@Test
	public void testBooking() {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		Booking myBooking = new Booking();
		myBooking.setBooking_number("delme");
		myBooking.setFirst_name("james test");
		myBooking.setSurname("delme");
		myBooking.setKreuzfahrt((float) 0.0);

		BookingService bookingService = (BookingService) context.getBean("bookingService");
		bookingService.persistBooking(myBooking);
		System.out.println("persisted Booking:  id=" + myBooking.getId());		
		myBooking.setMonth_departure(6);
		bookingService.updateBooking(myBooking);
		bookingService.deleteBooking(myBooking);
	}
	
	@Test
	public void test_readRecentBookings() {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		BookingService bookingService = (BookingService) context.getBean("bookingService");
		List<Booking> myBookings = bookingService.getTopNRows(20);
		
		Assert.assertTrue(myBookings.size() == 20) ;
		
		for (Booking myBooking : myBookings) {
			Assert.assertNotNull(myBooking.getId());
		}
	}
	
	@Test
	public void test_November2014Bookings() {
		// soll 17 sein .  History test
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		BookingService bookingService = (BookingService) context.getBean("bookingService");
		List<Booking> myBookings = bookingService.getBookingsByMonthYear(11, 2014, false);
		
		Assert.assertTrue(myBookings.size() == 27) ;
		Assert.assertFalse(myBookings.size() == 26) ;
	}

}
