package uk.ac.man.cs.eventlite.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@Controller
@RequestMapping(value = "/", produces = { MediaType.TEXT_HTML_VALUE })
public class HomepageController {

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;

	@RequestMapping(method = RequestMethod.GET)
	public String getAllEvents(Model model) {
		Iterable<Event> allEvents = eventService.findAll();
		model.addAttribute("events", allEvents);
		Queue<Event> upcoming = new LinkedList<Event>();
		Iterator<Event> eventIterator = allEvents.iterator();
		List<Venue> venList = new ArrayList<Venue>();
		venueService.findAll().iterator().forEachRemaining(venList::add);
		int[] count = new int[venList.size()];
		int index;
		Venue[] venArr = new Venue[venList.size()];
		venList.toArray(venArr);
		int maxInd;
		
		while (eventIterator.hasNext())
		{
			Event item = eventIterator.next();
			if (item.isUpcoming())
			{
				upcoming.add(item);
			}
			index = venList.indexOf(item.getVenue());

			count[index]++;
		}
		
		List<Event> nextThree = new LinkedList<Event>();
		List<Venue> threeVen = new LinkedList<Venue>();
		
		for (int i = 0; i<3; i++)
		{
			if (upcoming.size() > 0)
			{
				nextThree.add(upcoming.remove());
			}
			
		}
		int numVensDisp = venList.size();
		if (numVensDisp > 3)
		{
			numVensDisp = 3;
		}
		
		Integer[] countInteger = Arrays.stream( count ).boxed().toArray( Integer[]::new );
		for (int i = 0; i < numVensDisp; i++)
		{
			maxInd = Arrays.asList(countInteger).indexOf(Collections.max(Arrays.asList(countInteger)));
			threeVen.add(venArr[maxInd]);
			countInteger[maxInd] = -1;
		}
		
		
		
		model.addAttribute("three", nextThree);
		model.addAttribute("venues", threeVen);
		return "home/index";
	}

	
	@GetMapping("/view_event_details/{id}")
	public String viewEventDetails(@PathVariable("id") long id, Model model) {
		model.addAttribute("event", eventService.findOne(id));
		return "events/view_event_details";
	}


}