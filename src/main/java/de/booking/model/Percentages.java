package de.booking.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PERCENTAGES")
public class Percentages {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID" , nullable = false)
	private long id;
	
	@Column(name = "KREUZFAHRT_PERCENT")
	private float kreuzfahrt_percent ;
	
	@Column(name = "FLUG_PERCENT")
	private float flug_percent ;
	
	@Column(name = "HOTEL_PERCENT")
	private float hotel_percent ;
	
	@Column(name = "VERSICHERUNG_PERCENT")
	private float versicherung_percent ;

	public float getKreuzfahrt_percent() {
		return kreuzfahrt_percent;
	}

	public void setKreuzfahrt_percent(float kreuzfahrt_percent) {
		this.kreuzfahrt_percent = kreuzfahrt_percent;
	}

	public float getFlug_percent() {
		return flug_percent;
	}

	public void setFlug_percent(float flug_percent) {
		this.flug_percent = flug_percent;
	}

	public float getHotel_percent() {
		return hotel_percent;
	}

	public void setHotel_percent(float hotel_percent) {
		this.hotel_percent = hotel_percent;
	}

	public float getVersicherung_percent() {
		return versicherung_percent;
	}

	public void setVersicherung_percent(float versicherung_percent) {
		this.versicherung_percent = versicherung_percent;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}	
	

}
