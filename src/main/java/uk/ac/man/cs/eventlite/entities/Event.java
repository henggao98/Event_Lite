package uk.ac.man.cs.eventlite.entities;

import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
@Entity
@Table(name = "Events")
public class Event {
	@Id
	@GeneratedValue
	@Column(name="Event_Id")
	private long id;

	@Future (message = "The date must be in the future.")
	@NotNull(message = "There must be a date for the event.")
	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="Date")
	private LocalDate date;

	@JsonFormat(shape = JsonFormat.Shape.STRING)
	@DateTimeFormat(pattern = "HH:mm")
	@Column(name="Time")
	private LocalTime time;

	@NotEmpty(message = "The event must have a name.")
	@Size(max = 255, message = "The name of the event must not excede 255 characters.")
    @Column(name="Name")
	private String name;

	@Size(max = 500, message = "The description of the event must not excede 500 characters.")
	@Column(name="description")
	private String description;

	@NotNull(message = "The event must have a valid venue.")
	@ManyToOne
	@JoinTable(name="Venue_Id")
	private Venue venue;
	
	@Column(name="Event_Longitude")
    private double event_longitude ;

	@Column(name="Event_Latitude")
    private double event_latitude;

	public Event() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean isUpcoming() {
		if ((this.date.compareTo(LocalDate.now()) > 0) 
				|| (this.date.compareTo(LocalDate.now()) == 0 
				&& this.time.compareTo(LocalTime.now()) > 0))
		{
			return true;
		}
		return false;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}
	
	public LocalDate getDate() {
		return this.date;
	}
	
	public LocalTime getTime() {
		return time;
	}

	public void setTime(LocalTime time) {
		this.time = time;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Venue getVenue() {
		return venue;
	}
	
	public String getVenueName() {
		return venue.getName();
	}

	public void setVenue(Venue venue) {
		this.venue = venue;
	}
	
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getLongitude() {
		return event_longitude;
	}

	public void setLongitude(double event_longitude) {
		this.event_longitude = event_longitude;
	}
	
	public double getLatitude() {
		return event_latitude;
	}

	public void setLatitude(double event_latitude) {
		this.event_latitude = event_latitude;
	}
	
}
