package uk.ac.man.cs.eventlite.entities;

import twitter4j.Status;

public class Twitter{
	
	private Status status;
	
	public void setStatus(Status status) {
		this.status = status;
	}
	
	public Status getStatus() {
		return this.status;
	}
}