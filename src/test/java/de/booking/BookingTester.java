package de.booking;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.booking.graphics.HistoryBookings;
import de.booking.model.Booking;
import de.booking.service.BookingService;

public class BookingTester {
	
	private static ConfigurableApplicationContext context;

	@Test
	public void testTotal() {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		BookingService bookingService = (BookingService) context.getBean("bookingService");
		
		// add a booking for 100 Eu
		Booking myBooking = new Booking();
		myBooking.setFirst_name("test");
		myBooking.setSurname("delme");
		myBooking.setKreuzfahrt((float) 100.0);
		myBooking.setFlug(50);
		myBooking.setDay_departure(01);
		myBooking.setMonth_departure(01);
		myBooking.setYear_departure(1900);
		bookingService.insertNewBookingCalcTotal(myBooking);   
		
		Booking myBooking2 = new Booking();
		myBooking2.setFirst_name("test2");
		myBooking2.setSurname("delme2");
		myBooking2.setKreuzfahrt((float) 200.0);
		myBooking2.setFlug(100);
		myBooking2.setDay_departure(01);
		myBooking2.setMonth_departure(01);
		myBooking2.setYear_departure(1900);
		bookingService.insertNewBookingCalcTotal(myBooking2);
	
		List<Booking> myBookings = bookingService.getBookingsByMonthYear(01, 1900, false);
		
		HistoryBookings myHistoryBookings = new HistoryBookings();
		float totalAll = myHistoryBookings.getTotalAllMonths(myBookings);
		Assert.assertTrue(totalAll == 12.75) ;
		
		Float totalEhoi = myHistoryBookings.getTotalEHoiMonth(myBookings);
		Assert.assertTrue(totalEhoi == 450) ;
		
		bookingService.deleteBooking(myBooking);
		bookingService.deleteBooking(myBooking2);		
	}
	
	@Test
	public void testBookingDelete() {
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
		Booking myBooking = new Booking();
		myBooking.setBooking_number("delme");
		myBooking.setFirst_name("james test");
		myBooking.setSurname("delme");
		myBooking.setKreuzfahrt((float) 0.0);

		BookingService bookingService = (BookingService) context.getBean("bookingService");
		bookingService.insertNewBookingCalcTotal(myBooking);	
		myBooking.setMonth_departure(6);
		bookingService.updateBooking(myBooking);
		bookingService.deleteBooking(myBooking);
	}
	
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
