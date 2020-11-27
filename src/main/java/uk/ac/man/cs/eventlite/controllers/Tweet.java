package uk.ac.man.cs.eventlite.controllers;
import twitter4j.Status;

public class Tweet {
	private Status status;
	private String url;

	public Tweet(Status status)
	{
		this.status = status;
		this.url = "https://twitter.com/" + status.getUser().getScreenName() 
			    + "/status/" + status.getId();
	}
	
	public Status getStatus()
	{
		return this.status;
	}
	
	public String getUrl()
	{
		return this.url;
	}
}
