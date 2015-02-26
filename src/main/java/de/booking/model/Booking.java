package de.booking.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.swing.JLabel;
import javax.swing.JTextField;

@Entity
@Table(name = "BOOKING")
public class Booking {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID" , nullable = false)
	private long id;
	
	@Column(name = "KREUZFAHRT")
	private float kreuzfahrt ;
	
	@Column(name = "FLUG")
	private float flug ;

	@Column(name = "HOTEL")
	private float hotel;
	
	@Column(name = "VERSICHERUNG")
	private float versicherung;

	@Column(name = "TOTAL")
	private float total;

	@Column(name = "DAY_DEPARTURE")
	private int day_departure;
	
	@Column(name = "MONTH_DEPARTURE")
	private int month_departure;
	
	@Column(name = "YEAR_DEPARTURE")
	private int year_departure;	

	@Column(name = "SURNAME")
	private String surname;	
	
	@Column(name = "FIRST_NAME")
	private String first_name;	
	
	@Column(name = "BOOKING_NUMBER")
	private String booking_number;	
	
	@Column(name = "STORNO")
	private int storno;	

	@Column(name = "COMMENT")
	private String comment;	
	
	@Column(name = "BOOKING_DATE")
	private Date booking_date;	

	// current timestamp
	@Column(name = "UPDATED_TIME")
	private Date updated_time;	

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public float getKreuzfahrt() {
		return kreuzfahrt;
	}
	public void setKreuzfahrt(float kreuzfaht) {
		this.kreuzfahrt = kreuzfaht;
	}
	public float getFlug() {
		return flug;
	}
	public void setFlug(float flug) {
		this.flug = flug;
	}
	public float getHotel() {
		return hotel;
	}
	public void setHotel(float hotel) {
		this.hotel = hotel;
	}
	public float getVersicherung() {
		return versicherung;
	}
	public void setVersicherung(float versicherung) {
		this.versicherung = versicherung;
	}
	public float getTotal() {
		return total;
	}
	public void setTotal(float total) {
		this.total = total;
	}
	public int getDay_departure() {
		return day_departure;
	}
	public void setDay_departure(int day_departure) {
		this.day_departure = day_departure;
	}
	public int getMonth_departure() {
		return month_departure;
	}
	public void setMonth_departure(int month_departure) {
		this.month_departure = month_departure;
	}
	public int getYear_departure() {
		return year_departure;
	}
	public void setYear_departure(int year_departure) {
		this.year_departure = year_departure;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getBooking_number() {
		return booking_number;
	}
	public void setBooking_number(String integer) {
		this.booking_number = integer;
	}
	public int getStorno() {
		return storno;
	}
	public void setStorno(int storno) {
		this.storno = storno;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getBooking_date() {
		return booking_date;
	}
	public void setBooking_date(Date booking_date) {
		this.booking_date = booking_date;
	}
	public Date getUpdated_timee() {
		return updated_time;
	}
	public void setUpdated_timee(Date updated_timee) {
		this.updated_time = updated_timee;
	}
	
	
	public int setAllUserFields(JLabel[] labels, JTextField[] textFields) {
		int successful = 0;
		// this method is called by BookingHandler when book me button is clicked.
		for (int i = 0;i<labels.length;i++){
			String myLabel = labels[i].getText();
			String enteredText = textFields[i].getText();
			myLabel = myLabel.replace(":", "");
			myLabel = myLabel.replace("(dd/mm/yyyy)", "");
			if (myLabel.trim().equalsIgnoreCase("kreuzfahrt")) {
				setKreuzfahrt(Float.valueOf(enteredText));
			} else {
				successful += 1;
			}
			if (myLabel.trim().equalsIgnoreCase("flug")) {
				setFlug(Float.valueOf(enteredText));
			} else {
				successful += 1;
			}
			if (myLabel.trim().equalsIgnoreCase("hotel")) {
				setHotel(Float.valueOf(enteredText));
			} else {
				successful += 1;
			}
			if (myLabel.trim().equalsIgnoreCase("versicherung")) {
				setVersicherung(Float.valueOf(enteredText));
			} else {
				successful += 1;
			}
			if (myLabel.trim().equalsIgnoreCase("versicherung")) {
				setVersicherung(Float.valueOf(enteredText));
			} else {
				successful += 1;
			}
			if (myLabel.trim().equalsIgnoreCase("day_departure")) {
				setDay_departure(Integer.valueOf(enteredText));
			} else {
				successful += 1;
			}
			if (myLabel.trim().equalsIgnoreCase("month_departure")) {
				setMonth_departure(Integer.valueOf(enteredText));
			} else {
				successful += 1;
			}
			if (myLabel.trim().equalsIgnoreCase("year_departure")) {
				setYear_departure(Integer.valueOf(enteredText));
			} else {
				successful += 1;
			}
			if (myLabel.trim().equalsIgnoreCase("surname")) {
				setSurname(enteredText);
			} else {
				successful += 1;
			}
			if (myLabel.trim().equalsIgnoreCase("first_name")) {
				setFirst_name(enteredText);
			} else {
				successful += 1;
			}
			if (myLabel.trim().equalsIgnoreCase("booking_number")) {
				setBooking_number(enteredText );
			} else {
				successful += 1;
			}
			if (myLabel.trim().equalsIgnoreCase("booking_date")) {
				String[] ddmmyyyy = enteredText.split("/");
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.DAY_OF_MONTH,Integer.valueOf(ddmmyyyy[0]).intValue());
				cal.set(Calendar.MONTH,Integer.valueOf(ddmmyyyy[1]).intValue()-1);
				cal.set(Calendar.YEAR,Integer.valueOf(ddmmyyyy[2]).intValue());
				setBooking_date(cal.getTime());
			} else {
				successful += 1;
			}
			if (myLabel.trim().equalsIgnoreCase("storno")) {
				// no-storno/not-cancelled = 0, use this as default
				if (enteredText.length()==0) {
					setStorno(0);
				} else {
					setStorno(Integer.valueOf(enteredText));
				}
			} else {
				successful += 1;
			}
		}
		return successful ;
	}


}
