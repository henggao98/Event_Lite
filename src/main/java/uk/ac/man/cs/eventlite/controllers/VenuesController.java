package uk.ac.man.cs.eventlite.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import uk.ac.man.cs.eventlite.config.MapGeocoding;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;


@Controller
@RequestMapping(value = "/venues", produces = { MediaType.TEXT_HTML_VALUE })
public class VenuesController {
	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;
	
	@RequestMapping(method = RequestMethod.GET)
	public String getAllVenues(Model model) {


		model.addAttribute("venues", venueService.findAll());

		return "venues/index";
	}
	
	@RequestMapping(value = "/addVenue", method = RequestMethod.GET)
	public String newVenue(Model model) {
		if (!model.containsAttribute("venue")) {
			model.addAttribute("venue", new Venue());
		}
		
		return "venues/addVenue";
	}

	
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	public String createVenue(@RequestBody @Valid @ModelAttribute Venue venue,
			BindingResult errors, Model model, RedirectAttributes redirectAttrs) {

		if (errors.hasErrors()) {
			model.addAttribute("venue", venue);
			return "venues/addVenue";
		}
		
		MapGeocoding.setLonAndLat(venue);
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		venueService.save(venue);
		redirectAttrs.addFlashAttribute("ok_message", "New Venue added.");
		return "redirect:/venues";
	}

	@RequestMapping(value = "/view_venue_details/{id}", method = RequestMethod.GET)
	public String viewVenueDetails(@PathVariable("id") long id, Model model) {
		Venue venue = venueService.findOne(id);
		model.addAttribute("venue", venue);
		model.addAttribute("events", eventService.findByVenue(venue));
		return "venues/view_venue_details";
	}
	
	@GetMapping("/update/{id}")
	public String updateVenueForm(@PathVariable("id") long id, Model model) {
		model.addAttribute("venue", venueService.findOne(id));
		return "venues/update-venue-form";
	}
	
	@PostMapping("/updated/{id}")
	public String venueUpdated(@PathVariable("id") long id, Venue venue, BindingResult result, Model model) {
		if (result.hasErrors()) {
			System.out.println("got an error");
			venue.setId(id);
			return "events/update-venue-form";
		}
		System.out.println("got without error");
		
		MapGeocoding.setLonAndLat(venue);
		try {
			Thread.sleep(1000L);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		venueService.save(venue);
		model.addAttribute("venues", venueService.findAll());
		return "redirect:/venues";
	}

	@GetMapping("/search")
	public String searchVenueByName(Model model ,@RequestParam(value="name",required=false) String name,BindingResult result) {
		if (result.hasErrors()) {
			System.out.println("got an error");
			return "events/index";
		}
		Iterable<Venue> venues = venueService.findByName(name);
		model.addAttribute("venues", venues);
		return "venues/index";
	}
}

