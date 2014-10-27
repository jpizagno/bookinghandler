package de.booking;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.booking.dao.BookingDAO;
import de.booking.model.Booking;
import de.booking.service.BookingService;

public class BookingHandler {
	
	private static ConfigurableApplicationContext context;
	
	public static void main (String[] args) {
		BookingHandler myApp = new BookingHandler();
		myApp.setup();
		myApp.testBooking();
		myApp.shutdown();
	}
	
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
	
	public void setup() {
		System.out.println("load context");
		context = new ClassPathXmlApplicationContext("applicationContext.xml");
	}
	public void shutdown() {
		context.close();
	}

}
