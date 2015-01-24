package de.booking;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.booking.graphics.HistoryBookings;
import de.booking.model.Booking;
import de.booking.service.BookingService;

public class BookingTester {
	
	private static ConfigurableApplicationContext context;
	
	@Before
	public void cleanDatabase() {
		// setup the session factory
		AnnotationConfiguration configuration = new AnnotationConfiguration();

		configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5Dialect");
		configuration.setProperty("hibernate.connection.driver_class","com.mysql.jdbc.Driver");
		configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/bookings_test");
		configuration.setProperty("hibernate.connection.password", "root");
		configuration.setProperty("hibernate.connection.username", "root");

		SessionFactory sessionFactory = configuration.buildSessionFactory();
		Session session = sessionFactory.openSession();

		Transaction tx = session.beginTransaction();
		Query queryTruncate = session.createSQLQuery("DELETE FROM booking");
		queryTruncate.executeUpdate();
		session.flush();
		tx.commit();
	}
	
	@Test
	public void testBookingInsert() {
		context = new ClassPathXmlApplicationContext("applicationContext_junittest.xml");
		Booking myBooking = new Booking();
		myBooking.setBooking_number("delme");
		myBooking.setFirst_name("james test");
		myBooking.setSurname("delme");
		myBooking.setKreuzfahrt((float) 0.0);

		BookingService bookingService = (BookingService) context.getBean("bookingService");
		bookingService.insertNewBookingCalcTotal(myBooking);	
		//myBooking.setMonth_departure(6);
		//bookingService.updateBooking(myBooking);
		//bookingService.deleteBooking(myBooking);
	}
	
	@Test
	public void testBookingDelete() {
		context = new ClassPathXmlApplicationContext("applicationContext_junittest.xml");
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
	public void testBookingStorno() {
		context = new ClassPathXmlApplicationContext("applicationContext_junittest.xml");
		Booking myBooking = new Booking();
		myBooking.setBooking_number("delme");
		myBooking.setFirst_name("james test");
		myBooking.setSurname("delme");
		myBooking.setKreuzfahrt((float) 0.0);
		myBooking.setStorno(0);

		BookingService bookingService = (BookingService) context.getBean("bookingService");
		bookingService.insertNewBookingCalcTotal(myBooking);	
		
		// read bookings with storno = 0
		BookingService bookingService2 = (BookingService) context.getBean("bookingService");
		List<Booking> listNoStornoBookings = bookingService2.getBookingsWithStorno();
		Assert.assertTrue(listNoStornoBookings.size()==0);
		
		// update booking, set to stono
		myBooking.setStorno(1);
		bookingService.updateBooking(myBooking);
		
		// read booknigs with stonro = 1
		List<Booking> listStornoBookings = bookingService.getBookingsWithStorno();
		Assert.assertTrue(listStornoBookings.size()==1);
		
		bookingService.deleteBooking(myBooking);
	}
	
	@Test
	public void testTotal() {
		context = new ClassPathXmlApplicationContext("applicationContext_junittest.xml");
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

}
