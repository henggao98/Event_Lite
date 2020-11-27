package uk.ac.man.cs.eventlite.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import uk.ac.man.cs.eventlite.entities.Venue;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(value = "/api/venues", produces = { MediaType.APPLICATION_JSON_VALUE, MediaTypes.HAL_JSON_VALUE })
public class VenuesControllerApi {

	@Autowired
	private VenueService venueService;
	
	@Autowired
	private EventService eventService;

	@RequestMapping(method = RequestMethod.GET)
	public Resources<Resource<Venue>> getAllVenues() {
		return venueToResource(venueService.findAll());
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Resource<Venue> getVenue(@PathVariable("id") long id) {
		Venue venue = venueService.findOne(id);
		return venueToResource(venue);
	}
	
	
	@RequestMapping(value = "/{id}/next3events", method = RequestMethod.GET)
	public Resources<Event> getNext3Events(@PathVariable("id") long id) {
		Link selfLink = linkTo(methodOn(VenuesControllerApi.class).getNext3Events(id)).withSelfRel();
		Venue v = venueService.findOne(id);
		
		Iterable<Event> allEvents = eventService.findAll();
		
		List<Event> next3Events = StreamSupport.stream(allEvents.spliterator(), false)
				.limit(3).sorted((e1,  e2) -> e1.getDate().compareTo(e2.getDate())).collect(toList());
		
		return new Resources<Event>(next3Events, selfLink);
	}


	private Resource<Venue> venueToResource(Venue venue) {
		Link selfLink = linkTo(VenuesControllerApi.class).slash(venue.getId()).withSelfRel();
		Link eventLink = linkTo(VenuesControllerApi.class).slash(venue.getId()).slash("event").withRel("event");
		Link venueLink = linkTo(VenuesControllerApi.class).slash(venue.getId()).slash("venue").withRel("venue");
		Link next3EventsLink = linkTo(VenuesControllerApi.class).slash(venue.getId()).slash("next3events").withRel("next3events");
		return new Resource<Venue>(venue, eventLink, selfLink, venueLink, next3EventsLink);
	}

	private Resources<Resource<Venue>> venueToResource(Iterable<Venue> venues) {
		Link selfLink = linkTo(methodOn(VenuesControllerApi.class).getAllVenues()).withSelfRel();

		List<Resource<Venue>> resources = new ArrayList<Resource<Venue>>();
		for (Venue venue : venues) {
			resources.add(venueToResource(venue));
		}

		return new Resources<Resource<Venue>>(resources, selfLink);

	}
}
