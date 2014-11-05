package de.booking.toolbox;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import de.booking.database.DatabaseHandler;
import de.booking.model.Booking;

public class DataIngest {
	
	public static void IngestCSV(String filename) {
		// This method takes the name of a comma separated variable file of previous bookings
		//    and ingest this into the database. The file should be of the format:
		// kreuzfahrt,flug,hotel,versicherung,total,day_departure,month_departure,year_departure,surname,first name,booking number ,date_booking,storno
		// 1916.00,0,0,0,67.06,22,4,2011,Tessmer,erwin,112066,8/3/2011,STORNO
		
		// open DB:
		DatabaseHandler myDB = new DatabaseHandler();
		
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
						 myBooking.setKreuzfahrt(strLine.split(",")[0]);
						 myBooking.setFlug(strLine.split(",")[1]);
						 myBooking.setHotel(strLine.split(",")[2]);
						 myBooking.setVersicherung(strLine.split(",")[3]);
						 // total set by trigger
						 myBooking.setDayDeparture(strLine.split(",")[5]);
						 myBooking.setMonthDeparture(strLine.split(",")[6]);
						 myBooking.setYearDeparture(strLine.split(",")[7]);
						 myBooking.setSurname(strLine.split(",")[8]);
						 myBooking.setFirstName(strLine.split(",")[9]);
						 myBooking.setBookingNumber(strLine.split(",")[10]);
						 myBooking.setBookingDate(strLine.split(",")[11]);
						 if (strLine.split(",").length==13) {
							 // then have Storno:
							 myBooking.setStorno("STORNO");
						 }
						 
						 // insert bookikng into DB
						 myDB.insertNewBooking(myBooking);
					 } 
				 }
				 //Close the input stream
				 in.close();
			} catch (FileNotFoundException e) {
				System.out.println("DataInget.IngestCSV() ERROR:  FileInputStream(filename);");
				System.out.println("DataInget.IngestCSV() ERROR: filename = "+filename);
				e.printStackTrace();
			} catch (IOException e) {
				System.out.println("DataInget.IngestCSV() ERROR: strLine = br.readLine()) ");
				e.printStackTrace();
			}
		 myDB.disconnect();
	}

}
