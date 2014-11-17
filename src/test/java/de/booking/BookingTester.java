package de.booking;

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
	
	
}
