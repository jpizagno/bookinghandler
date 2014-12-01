package de.booking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import de.booking.model.Booking;
import de.booking.service.BookingService;
import de.booking.toolbox.OSDetector;

public class DatabaseHandler {
	private Connection conn = null;
	private boolean connected = false;

	private static ConfigurableApplicationContext context;

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public DatabaseHandler() {
		context = new ClassPathXmlApplicationContext("classpath*:**/applicationContext.xml");
		connect();
	}

	/**
	 *  connect to MySQL database
	 *  
	 *  */
	public boolean connect() {
		context = new ClassPathXmlApplicationContext("classpath*:**/applicationContext.xml");

		try
		{
			String userName = "juliap";
			String password = "lulustinks";
			String url = "jdbc:mysql://localhost/bookings";
			Class.forName ("com.mysql.jdbc.Driver").newInstance ();
			conn = DriverManager.getConnection (url, userName, password);
			// try to start autocommit=1 (on) so that updated table UpdateRow() works in real time:
			String query = "set autocommit=1;";
			Statement stmt = conn.createStatement();
			stmt.execute(query);
			stmt.close();
			System.out.println ("Database connection established");
			connected = true;
		}
		catch (Exception e)
		{
			System.err.println ("Cannot connect to database server");
			connected = false;
		}
		return connected;
	}

	/**
	 * disconnect from database
	 * @return boolean
	 */
	public boolean disconnect() {
		boolean worked = false;
		if (conn!=null){
			try
			{
				conn.close();
				conn = null;
				System.out.println ("Database connection terminated");
				worked = true;
			}
			catch (Exception e) { 
				worked = false; 
			}
		} else {
			// assume no connection if conn==null:, so not connected.
			worked = true;
		}
		return worked;
	}

	/**
	 * this method attempts to SELECT * tables into OUTFILES for tables=booking,percentages,total.
	 */
	public void exportOUTFILEonStartUp() {
		String outfile = "";
		Date now = new Date();
		if (OSDetector.isMac()){
			outfile = "/tmp/"+now;
		} else {
			// assume windows
			outfile = "/Users/juliapizagno/bookings/"+now;
			// windows does not allow : in filenames.   so replace with _
			outfile = "C:"+outfile.replace(":", "_");
		}
		outfile=outfile.replace(" ", "");
		if (conn!=null){ // already connected
			String query_all = "SELECT *";
			query_all = query_all + " INTO OUTFILE '"+outfile+".booking.csv'";
			query_all = query_all + " FIELDS TERMINATED BY ',' ";
			query_all = query_all + " ENCLOSED BY '\"' ";
			query_all = query_all + " LINES TERMINATED BY '\\n'";
			query_all = query_all + " FROM booking ; ";
			System.out.println("*** DatabaseHander.exportOUTFULEonStartUp  query_all = "+query_all);
			try {
				Statement stmt = conn.createStatement();
				stmt.execute(query_all);
				stmt.close();
				String st="Wrote to Backup file to "+outfile;
				JOptionPane.showMessageDialog(null,st);
			} catch (SQLException e) {
				System.out.println("****** could not create a statement");
				System.out.println("DatabaseHandler.exportOUTFILEonStartUp():");
				e.printStackTrace();
				System.out.println("query_all 1 = ");
				System.out.println(query_all);
				String st="something wrong with query_all = "+query_all;
				JOptionPane.showMessageDialog(null,st);
			}

			query_all =  " SELECT *";
			query_all = query_all + " INTO OUTFILE '"+outfile+".percentages.csv'";
			query_all = query_all + " FIELDS TERMINATED BY ',' ";
			query_all = query_all + " ENCLOSED BY '\"' ";
			query_all = query_all + " LINES TERMINATED BY '\\n'";
			query_all = query_all + " FROM percentages ; ";
			try {
				Statement stmt = conn.createStatement();
				stmt.execute(query_all);
				stmt.close();
			} catch (SQLException e) {
				System.out.println("****** could not create a statement");
				System.out.println("DatabaseHandler.exportOUTFILEonStartUp():");
				e.printStackTrace();
				System.out.println("query_all 3 = ");
				System.out.println(query_all);
			}
		} else {
			System.out.println("DatabaseHandler.exportOUTFILEonStartUp(): DB not connected");
		}	
	}

	/**
	 *  get defaults for display
	 *  defaults include:
	 *  	i.e. kreuzfahrt:0.0035 , flug:0.015 , hotel: 0.015 , versicherung: 0.05
	 *     current_month_viewing = "April"
	 *  
	 * @return
	 */
	public String getDefaults(){
		StringBuilder myAnswer = new StringBuilder();
		myAnswer.append("my defaults");
		return myAnswer.toString();
	}

	/**
	 * storno a booking
	 * cancel/storno=1  valid=0
	 * 
	 * @param bookingUnstorno
	 */
	public void UNstornoBooking(Booking bookingUnstorno) {
		if (conn!=null){ // already connected
			try {
				Statement stmt = conn.createStatement();
				String SQLstring = "UPDATE booking SET storno=0 WHERE booking_number="+bookingUnstorno+" ; ";
				System.out.println("DatabaseHandler.UNstornoBooking()  SQLstring:  ");
				System.out.println(SQLstring);
				stmt.execute(SQLstring);
				stmt.close();
			} catch (SQLException e) {
				System.out.println("****** could not create a statement");
				System.out.println("DatabaseHandler.UNstornoBooking():");
				e.printStackTrace();
			}
		} else {
			System.out.println("DatabaseHandler.UNstornoBooking(): DB not connected");
		}	
	}

	/**
	 *  storno a booking
	 * @param booking_number
	 */
	public void stornoBooking(String booking_number) {
		if (conn!=null){ // already connected
			try {
				Statement stmt = conn.createStatement();
				String SQLstring = "UPDATE booking SET storno=1 WHERE booking_number="+booking_number+" ; ";
				System.out.println("DatabaseHandler.stornoBooking()  SQLstring:  ");
				System.out.println(SQLstring);
				stmt.execute(SQLstring);
				stmt.close();
			} catch (SQLException e) {
				System.out.println("****** could not create a statement");
				System.out.println("DatabaseHandler.stornoBooking():");
				e.printStackTrace();
			}
		} else {
			System.out.println("DatabaseHandler.stornoBooking(): DB not connected");
		}	
	}


	/**
	 * delete a booking
	 * 
	 * @param booking_number
	 */
	public void deleteBooking(String booking_number) {
		if (conn!=null){ // already connected
			try {
				Statement stmt = conn.createStatement();
				String SQLstring = "DELETE FROM booking WHERE booking_number=\'"+booking_number+"\' ; ";
				System.out.println("deleteBooking()  SQLstring:  ");
				System.out.println(SQLstring);
				stmt.execute(SQLstring);
				stmt.close();
			} catch (SQLException e) {
				System.out.println("****** could not create a statement");
				System.out.println("DatabaseHandler.deteleBooking():");
				e.printStackTrace();
			}
		} else {
			System.out.println("DatabaseHandler.deteleBooking(): DB not connected");
		}	
	}

	/**
	 * if any String is passed, then selects bookings with storno=0.  returns 
	 * the total for ehoi, which is kreuzfahrt
	 * 
	 * @param month
	 * @param year
	 * @return
	 */
	public List<Booking> getBookingsEhoiByMonthYear(int month, int year) {
		BookingService bookingService = (BookingService) context.getBean("bookingService");

		return bookingService.getBookingsByMonthYear(month, year, false);
	}

	// create table statements
	public static String create_table_booking = "create table booking (kreuzfahrt float(8,4), flug float(8,4), versicherung float(8,4), " +
			"hotel float(8,4), total float(8,4), day_departure int(11), month_departure int(11), " +
			"year_departure int(11), surname varchar(100), first_name varchar(100), booking_number int(11), " +
			"booking_date date, storno boolean, updated_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, " +
			"comment varchar(2000) PRIMARY KEY (booking_number) );";

	public static String create_percentages = "(kreuzfahrt_percent float(8,4), flug_percent float(8,4), " +
			"hotel_percent float(8,4), versicherung float(8,4));";
	private static Float kreuzfahrt_percent_default = 0.035f;
	private static Float flug_percent_default = 0.015f;
	private static Float hotel_percent_default = 0.015f;
	private static Float versicherung_percent_default = 0.05f;
	public static String insert_default_percetages = "insert into percentages (kreuzfahrt_percent," +
			"flug_percent,hotel_percent,versicherung_percent) values (" +
			""+kreuzfahrt_percent_default+"" +
			","+flug_percent_default+"," +
			""+hotel_percent_default+","+versicherung_percent_default+");";

}
