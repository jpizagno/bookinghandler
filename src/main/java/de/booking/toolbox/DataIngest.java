package de.booking.toolbox;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.booking.model.Booking;
import de.booking.service.BookingService;

public class DataIngest {


	private static ConfigurableApplicationContext context;

	/**
	 * This method takes the name of a comma separated variable file of previous 
	 *     bookings and ingest this into the database. 
	 *     The file should be of the format:
	 *     kreuzfahrt,flug,hotel,versicherung,total,day_departure,month_departure,year_departure,surname,first name,booking number ,date_booking,storno
	 *       1916.00,0,0,0,67.06,22,4,2011,Tessmer,erwin,112066,8/3/2011,STORNO
	 * 
	 * */
	@SuppressWarnings("deprecation")
	public static void IngestCSV(String filename) {
		context = new ClassPathXmlApplicationContext("classpath*:**/applicationContext.xml");

		//try {
		FileInputStream fstream;
		try {
			fstream = new FileInputStream(filename);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			//Read File Line By Line
			while ((strLine = br.readLine()) != null)   {
				// make a booking for each line.
				// check first for line format.
				if (strLine.contains("flug")) {
					// This is header.  For now, maybe skip 
				} else {
					// make a booking from this line:
					Booking myBooking = new Booking();
					myBooking.setKreuzfahrt(Float.valueOf( strLine.split(",")[0] ));
					myBooking.setFlug(Float.valueOf( strLine.split(",")[1]));
					myBooking.setHotel(Float.valueOf( strLine.split(",")[2]));
					myBooking.setVersicherung(Float.valueOf( strLine.split(",")[3]));
					// total set by trigger
					myBooking.setDay_departure(Integer.valueOf( strLine.split(",")[5]));
					myBooking.setMonth_departure(Integer.valueOf( strLine.split(",")[6]));
					myBooking.setYear_departure(Integer.valueOf( strLine.split(",")[7]));
					myBooking.setSurname(strLine.split(",")[8]);
					myBooking.setFirst_name(strLine.split(",")[9]);
					myBooking.setBooking_number(strLine.split(",")[10]);
					String[] ddmmyyyy = strLine.split(",")[11].split("/");
					Date temp_booking_date = new Date(Integer.valueOf(ddmmyyyy[2]).intValue(),
							Integer.valueOf(ddmmyyyy[1]).intValue(), 
							Integer.valueOf(ddmmyyyy[0]).intValue());
					myBooking.setBooking_date(temp_booking_date);
					if (strLine.split(",").length==13) {
						// then have Storno:
						myBooking.setStorno(1);
					}

					// insert bookikng into DB
					//myDB.insertNewBooking(myBooking);
					BookingService bookingService = (BookingService) context.getBean("bookingService");
					bookingService.insertNewBookingCalcTotal(myBooking);
				} 
			}
			//Close the input stream
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
