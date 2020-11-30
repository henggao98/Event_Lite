package uk.ac.man.cs.eventlite.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import uk.ac.man.cs.eventlite.config.MapGeocoding;
import uk.ac.man.cs.eventlite.config.Twitter4j;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping(value = "/events", produces = { MediaType.TEXT_HTML_VALUE })
public class EventsController {

	@Autowired
	private EventService eventService;
	
	@Autowired
	private VenueService venueService;
	
	Twitter twitter = Twitter4j.configureTwitter4j();
	
	@RequestMapping(method = RequestMethod.GET)
	public String getAllEvents(Model model) throws TwitterException {
		Iterable<Event> allEvents = eventService.findAll();
		List<Event> upcoming = new LinkedList<Event>();
		List<Event> previous = new LinkedList<Event>();
		Iterator<Event> eventIterator = allEvents.iterator();
		Tweet tweet;

		while (eventIterator.hasNext())
		{
			Event item = eventIterator.next();
			if (item.isUpcoming()) {
				upcoming.add(item);
			} else {
				previous.add(item);
			}
		}
//		List<Status> tweets = twitter.getHomeTimeline();
//		List<Tweet> fiveTweets = new LinkedList<Tweet>();
//		int numTweetsDisp = tweets.size();
//		if (numTweetsDisp > 5)
//		{
//			numTweetsDisp = 5;
//		}
//		for (int i = 0; i < numTweetsDisp; i++)
//		{
//			tweet = new Tweet(tweets.get(i));
//			fiveTweets.add(tweet);
//		}
//		model.addAttribute("tweets", fiveTweets);
		model.addAttribute("upcoming", upcoming);
		model.addAttribute("previous", previous);
		return "events/index";
	}

	@RequestMapping(value="/addEvent", method= RequestMethod.GET)
	public String getAddNewEvent(Model model, HttpServletRequest request) {
		if (!model.containsAttribute("event")) {
			model.addAttribute("event", new Event());
		}
		
		Iterable<Venue> allVenues = venueService.findAll();
		model.addAttribute("allVenues", allVenues);

		return "events/addEvent";
	}

	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String addEvent(@RequestBody @Valid @ModelAttribute Event event,
			BindingResult errors, Model model, RedirectAttributes redirectAttrs) {

		//System.out.println("\n\n\n\n ========================================================= \n\n\n\n\n\n");
		if (errors.hasErrors()) {
			model.addAttribute("event", event);
			
			//redirectAttrs.addFlashAttribute("ok_message", "New event added.");
			return "events/addEvent";
		}
				
				
	    //--------------------------------------------------------------------------------------------------------

		MapGeocoding.setLonAndLat(event);
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		eventService.save(event);
		redirectAttrs.addFlashAttribute("ok_message", "New event added.");

		return "redirect:/events";
	}

	@GetMapping("/update/{id}")
	public String updateEventForm(@PathVariable("id") long id, Model model) {
		model.addAttribute("event", eventService.findOne(id));
		return "events/update-event-form";
	}

	@PostMapping("updated/{id}")
	public String eventUpdated(@PathVariable("id") long id, Event event, BindingResult result, Model model) {
		LocalDate dateNow = LocalDate.now();
		if (event.getDate().compareTo(dateNow) <= 0) {
			System.out.println("Date must be in the future");
			return "events/update-event-form";
		}
		else if (result.hasErrors()) {
			System.out.println("got an error");
			event.setId(id);
			return "events/update-event-form";
		}
		System.out.println("got without error");
		
		MapGeocoding.setLonAndLat(event);
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		eventService.save(event);
		model.addAttribute("events", eventService.findAll());
		return "redirect:/events";
	}

	@GetMapping("/view_event_details/{id}")
	public String viewEventDetails(@PathVariable("id") long id, Model model) throws InterruptedException {

		Event event = eventService.findOne(id);	
		model.addAttribute("event", event);
		return "events/view_event_details";
	}


	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable("id") long id) {
		eventService.deleteById(id);

		return "redirect:/events";
	}


	@GetMapping(value = "/")
	public String getNameSpecifiedEvents(@RequestParam(value = "sname", required = false) String name, Model model) {
		Iterable<Event> allEvents = eventService.findByName("%" + name + "%");
		List<Event> upcoming = new LinkedList<Event>();
		List<Event> previous = new LinkedList<Event>();
		Iterator<Event> eventIterator = allEvents.iterator();

		while (eventIterator.hasNext()) {
			Event item = eventIterator.next();
			if (item.isUpcoming()) {
				upcoming.add(item);
			}
			else
			{
				previous.add(item);
			}
		}
		model.addAttribute("upcoming", upcoming);
		model.addAttribute("previous", previous);
		return "events/index";
	}
	
	
	@RequestMapping("/post_tweet/{id}")
	public String post_tweet(@RequestParam(value = "event_tweet") String event_tweet, @PathVariable(value = "id") long id, Model model){
		String tweet_build = event_tweet;
		Event event = eventService.findOne(id);
		tweet_build = tweet_build + " " + event.getName() + " at " + event.getVenueName() + " http://localhost:8080/events/view_event_details/" + id;
		System.out.println("What will be tweeted: " + tweet_build);
		try {twitter.updateStatus(tweet_build);} 
		catch (TwitterException e) {e.printStackTrace();}
		return "redirect:/events/view_event_details/" + id;
	}
	
}
