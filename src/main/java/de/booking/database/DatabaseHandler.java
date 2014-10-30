package de.booking.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.JOptionPane;

import de.booking.toolbox.OSDetector;
import BookingModel.Booking;

public class DatabaseHandler {
	private Connection conn = null;
	private boolean connected = false;
	
	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public DatabaseHandler() {
		connect();
	}
	
	// connect to MySQL database
	public boolean connect() {
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
	
	public void exportOUTFILEonStartUp() {
		/*
		 * this method attempts to SELECT * tables into OUTFILES for tables=booking,percentages,total.
		 */
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
			
		/*	query_all =  " SELECT *";
			query_all = query_all + " INTO OUTFILE '"+outfile+".total.csv'";
			query_all = query_all + " FIELDS TERMINATED BY ',' ";
			query_all = query_all + " ENCLOSED BY '\"' ";
			query_all = query_all + " LINES TERMINATED BY '\\n'";
			query_all = query_all + " FROM total ; ";
			try {
				Statement stmt = conn.createStatement();
				stmt.execute(query_all);
				stmt.close();
			} catch (SQLException e) {
				System.out.println("****** could not create a statement");
				System.out.println("DatabaseHandler.exportOUTFILEonStartUp():");
				e.printStackTrace();
				System.out.println("query_all 2 = ");
				System.out.println(query_all);
			}
		*/
			
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
	
	// get defaults for display
	public String getDefaults(){
		// defaults include:  
		//		i.e. kreuzfahrt:0.0035 , flug:0.015 , hotel: 0.015 , versicherung: 0.05
		//      current_month_viewing = "April"
		StringBuilder myAnswer = new StringBuilder();
		myAnswer.append("my defaults");
		return myAnswer.toString();
	}

	/* public String getInnodbStatus() {
		// This must have been for testing. feel free to delete.
		Statement stmt;
		String status = "did not work";
		try {
			stmt = conn.createStatement();
			String SQLstring = "show engine innodb status;";
			ResultSet rset = stmt.executeQuery(SQLstring); 
			status = "";
			status += " test ";
			
			while (rset.next()) {
				//status += rset.getString(1);
				for (int column=1; column<rset.getMetaData().getColumnCount();column++) {
					status += rset.getString(column);
					System.out.println("*** rset.getString(column) = "+rset.getString(column));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}
	*/
	
	// storno a booking
		public void UNstornoBooking(String booking_number) {
			if (conn!=null){ // already connected
				try {
					Statement stmt = conn.createStatement();
					String SQLstring = "UPDATE booking SET storno=0 WHERE booking_number="+booking_number+" ; ";
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
	
	// storno a booking
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
		
	
	// delete a booking
	public void deleteBooking(String booking_number) {
		if (conn!=null){ // already connected
			try {
				Statement stmt = conn.createStatement();
				String SQLstring = "DELETE FROM booking WHERE booking_number=\'"+booking_number+"\' ; ";
				System.out.println("deleteBooking()  SQLstring:  ");
				System.out.println(SQLstring);
				stmt.execute(SQLstring);

				/*// now delete from total:
				String SQLstring_total = "DELETE FROM total WHERE booking_number="+booking_number+" ; ";
				System.out.println("SQLstring_total:  ");
				System.out.println(SQLstring_total);
				stmt.execute(SQLstring);
				*/
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
	
	// insert a booking
	public void insertNewBooking(Booking booking2Insert) {
		// insert this booking into the database
		if (conn!=null){ // already connected
			try {
				Statement stmt = conn.createStatement();
				
				// get percentages
				Hashtable<String, Float> percentages_hashtable = getPercentages();
				
				// get total for this booking:
				float total = percentages_hashtable.get(kreuzfahrt_percent_column) * booking2Insert.getkreuzfahrt();
				total = total + percentages_hashtable.get(this.flug_percent_column) * booking2Insert.getflug();
				total = total + percentages_hashtable.get(this.hotel_percent_column) * booking2Insert.gethotel();
				total = total + percentages_hashtable.get(this.versicherung_percent_column) * booking2Insert.getversicherung();
				
				Date bookingdate = booking2Insert.getbookingdate();
				String bookingdate_sql = ""+String.valueOf(bookingdate.getYear())+"-" +
						""+String.valueOf(bookingdate.getMonth())+"-" +
						""+String.valueOf(bookingdate.getDate())+"";
				
				String insertString = "INSERT INTO booking (kreuzfahrt,flug,hotel,total," +
						"versicherung,day_departure,month_departure,year_departure," +
						"surname,first_name,booking_number,booking_date,storno) " +
						"VALUES ("+booking2Insert.getkreuzfahrt().toString()+"," +
								""+booking2Insert.getflug().toString()+"," +
								""+booking2Insert.gethotel().toString()+"," +
								""+String.valueOf(total)+"," +
								""+booking2Insert.getversicherung().toString()+"," +
								""+booking2Insert.getdaydeparture().toString()+"," +
								""+booking2Insert.getmonthdeparture().toString()+"," +
								""+booking2Insert.getyeardeparture().toString()+"," +
								"\'"+booking2Insert.getsurname().toString()+"\'," +
								"\'"+booking2Insert.getfirstname().toString()+"\'," +
								"\'"+booking2Insert.getbookingnumber().toString()+"\'," +
								"\'"+bookingdate_sql+"\'," +
								""+booking2Insert.getstorno().toString()+");";
				System.out.println("Executing string = "+insertString);
				stmt.executeUpdate(insertString);
				stmt.close();
			} catch (SQLException e) {
				System.out.println("****** could not create a statement");
				e.printStackTrace();
			}
		} else {
			System.out.println("DB not connected");
		}		
	}

	public ResultSet getTopRows(int limitRows) {
		ResultSet rset = null;
		if (connected) {
			String[] myFields = Booking.returnFieldnames().split(":");
			String sSQL  = "SELECT "+Booking.returnFieldnames().replace(":"," , ")+" FROM booking order by updated_time desc LIMIT 0,";
			sSQL = sSQL + limitRows + " ;";
			System.out.println("sSQL = "+sSQL);
			Statement stmt;
			try {
				stmt = this.conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
				this.conn.setAutoCommit(true);
				rset = stmt.executeQuery(sSQL);
			} catch (SQLException e) {
				System.out.println("DatabaseHandler.get5TopRows()");
				e.printStackTrace();
			}
		}
		 return rset;
	}
	
	// get percentages
	public Hashtable<String,Float> getPercentages() {
		Hashtable<String,Float> percentages = new Hashtable<String,Float>();
		Statement stmt;
		ResultSet rset;
		String sSQL  = "SELECT * from percentages;";
		try {
			stmt = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rset = stmt.executeQuery(sSQL); 
			while (rset.next()) {
				percentages.put(kreuzfahrt_percent_column, rset.getFloat(kreuzfahrt_percent_column)) ;
				percentages.put(flug_percent_column, rset.getFloat(flug_percent_column)) ;
				percentages.put(hotel_percent_column, rset.getFloat(hotel_percent_column)) ;
				percentages.put(versicherung_percent_column, rset.getFloat(versicherung_percent_column)) ;
			}
		} catch (SQLException e) {
			System.out.println("DatabaseHandler.getPercentages()");
			e.printStackTrace();
		}
		return percentages;
	}
	
	// select bookings by month/year
	public ResultSet getBookingsByMonthYear(int month, int year) {
		ResultSet rset = null;
		if (connected) {
			String[] myFields = Booking.returnFieldnames().split(":");
			String selectClause = "";
			for(int i=0;i<myFields.length;i++) {
				selectClause += "bb."+myFields[i]+",";
			}
			selectClause = selectClause.substring(0, selectClause.length() - 1); // remove last comma
			String sSQL  = "SELECT "+selectClause+"  FROM booking as bb " +
					" WHERE bb.month_departure="+String.valueOf(month)+"" +
					" AND bb.year_departure="+String.valueOf(year)+"  ;";
			System.out.println("sSQL = "+sSQL);
			Statement stmt;
			try {
				stmt = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rset = stmt.executeQuery(sSQL);
			} catch (SQLException e) {
				System.out.println("DatabaseHandler.get5TopRows()");
				e.printStackTrace();
			}
		}
		 return rset;
	}

	
	// select bookings by month/year
	// OVERLOADED METHOD for getting bookings that have NOT been cancelled:
	public ResultSet getBookingsByMonthYear(int month, int year, String notused) {
		// if any STting is passed, then selects bookings with storno=0.
		ResultSet rset = null;
		if (connected) {
			String[] myFields = Booking.returnFieldnames().split(":");
			String selectClause = "";
			for(int i=0;i<myFields.length;i++) {
				selectClause += "bb."+myFields[i]+",";
			}
			selectClause = selectClause.substring(0, selectClause.length() - 1); // remove last comma
			String sSQL  = "SELECT "+selectClause+" FROM booking as bb " +
					" WHERE bb.month_departure="+String.valueOf(month)+"" +
					" AND bb.year_departure="+String.valueOf(year)+" " +
							" AND bb.storno=0 ;";
			System.out.println("sSQL = "+sSQL);
			Statement stmt;
			try {
				stmt = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rset = stmt.executeQuery(sSQL);
			} catch (SQLException e) {
				System.out.println("DatabaseHandler.etBookingsByMonthYear()");
				e.printStackTrace();
			}
		}
		 return rset;
	}
	
	public ResultSet getBookingsEhoiByMonthYear(int month, int year, String notused) {
		// if any STting is passed, then selects bookings with storno=0.
		// returns the total for ehoi, which is kreuzfahrt
		ResultSet rset = null;
		if (connected) {
			String[] myFields = Booking.returnFieldnames().split(":");
			String selectClause = "SELECT kreuzfahrt,flug,hotel,versicherung ";
			String sSQL  = selectClause+" FROM booking as bb " +
					" WHERE bb.month_departure="+String.valueOf(month)+"" +
					" AND bb.year_departure="+String.valueOf(year)+" " +
							" AND bb.storno=0 ;";
			System.out.println("sSQL = "+sSQL);
			Statement stmt;
			try {
				stmt = this.conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				rset = stmt.executeQuery(sSQL);
			} catch (SQLException e) {
				System.out.println("DatabaseHandler.getBookingsEhoiByMonthYear()");
				e.printStackTrace();
			}
		}
		 return rset;
	}

	
	// remove a booking

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
	private static String kreuzfahrt_percent_column = "kreuzfahrt_percent" ;
	private static String flug_percent_column = "flug_percent" ;
	private static String hotel_percent_column = "hotel_percent" ;
	private static String versicherung_percent_column = "versicherung_percent" ;
	public static String insert_default_percetages = "insert into percentages (kreuzfahrt_percent," +
			"flug_percent,hotel_percent,versicherung_percent) values (" +
			""+kreuzfahrt_percent_default+"" +
					","+flug_percent_default+"," +
					""+hotel_percent_default+","+versicherung_percent_default+");";
	/* not used (10 May):  public static String create_total_table = "CREATE TABLE total (booking_number int(11), 
	 * total float(8,4) PRIMARY KEY (booking_number) );";
	 */
	/* not used:  public static String total_booking_trigger = "use bookings; "+
						"delimiter $$"+
						"CREATE TRIGGER total_trig AFTER insert on booking"+
						"FOR EACH ROW"+
						"BEGIN"+
						"DECLARE kp FLOAT;"+
						"DECLARE fp FLOAT;"+
						"DECLARE hp FLOAT;"+
						"DECLARE vp FLOAT;"+
						"DECLARE k FLOAT;"+
						"DECLARE f FLOAT;"+
						"DECLARE h FLOAT;"+
						"DECLARE v FLOAT;"+
						"DECLARE mybooking_number INT;"+
						"SELECT kreuzfahrt_percent,flug_percent,hotel_percent,versicherung_percent INTO kp,fp,hp,vp FROM percentages;"+
						"SET @mytotal = NEW.kreuzfahrt*kp + NEW.flug*fp + NEW.hotel*hp + NEW.versicherung*vp;"+
						"INSERT into total (total,booking_number) values (@mytotal,NEW.booking_number);"+
						"END$$"+
						"delimiter ; ";
						*/
	
}
