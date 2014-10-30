package de.booking;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import de.booking.model.Booking;
import de.booking.service.BookingService;

public class BookingTester {
	
	private static ConfigurableApplicationContext context;

	@Test
	public void testBooking() {
		Booking myBooking = new Booking();
		myBooking.setBooking_number("delme");
		myBooking.setFirst_name("james test");
		myBooking.setSurname("delme");
		myBooking.setKreuzfaht((float) 0.0);

		BookingService bookingService = (BookingService) context.getBean("bookingService");
		bookingService.persistBooking(myBooking);
		System.out.println("persisted Employee:  id=" + myBooking.getId());		
		myBooking.setMonth_departure(6);
		bookingService.updateBooking(myBooking);
		bookingService.deleteBooking(myBooking);
	}
	
	
}
