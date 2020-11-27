package uk.ac.man.cs.eventlite.entities;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.*;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import uk.ac.man.cs.eventlite.config.MapGeocoding;

import org.hibernate.annotations.Formula;

@Entity
@Table(name = "venues")
public class Venue {

	@Id
	@GeneratedValue
	@Column(name="Venue_Id")
	private long id;
	
	@Size(max = 256, message = "The name of the venue must not excede 256 characters.")
	@NotEmpty(message = "The venue must have a name.")
	@Column(name="Name")
	private String name;
		
	@NotNull(message = "Capacity must be given.")
	@Min(value = 0, message="The capacity has to be a positive number.")
	@Column(name="Capacity")
	private int capacity;
	
	@NotEmpty(message = "The venue must have an address.")
	@Size(max = 300, message = "The address of the venue must not excede 300 characters.")
	@Column(name="Address")
	private String address;
	
	@NotEmpty(message = "The venue must have a postcode(ex:M13 9PR).")
	@Pattern(regexp ="[A-Za-z]{1,2}[0-9Rr][0-9A-Za-z]? [0-9][ABD-HJLNP-UW-Zabd-hjlnp-uw-z]{2}")
	@Column(name="Postcode")
	private String postcode;
	
	@Column(name="Latitude")
	private double venue_latitude;
	
	@Column(name="Longitude")
	private double venue_longitude;

	@Column(name="Events")
	@OneToMany
	private List<Event> events;
	
	public Venue()
	{
	}
	
	public Venue(long id, String name,  int capacity, String address, String postcode) {
		setId(id);
		setName(name);
		setCapacity(capacity);
		setAddress(address);
		setPostcode(postcode);
	}
	
	public Venue(String name,  int capacity, String address, String postcode) {
		setName(name);
		setCapacity(capacity);
		setAddress(address);
		setPostcode(postcode);
	}
	public List<Event> getEvents()
	{
		return events;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	
	public double getLongitude() {
		return venue_longitude;
	}

	public void setLongitude(double venue_longitude) {
		this.venue_longitude = venue_longitude;
	}
	
	public double getLatitude() {
		return venue_latitude;
	}

	public void setLatitude(double venue_latitude) {
		this.venue_latitude = venue_latitude;
	}
}
