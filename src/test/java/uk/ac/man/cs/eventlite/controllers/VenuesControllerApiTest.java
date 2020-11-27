package uk.ac.man.cs.eventlite.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import uk.ac.man.cs.eventlite.EventLite;
import javax.servlet.Filter;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.IsNot.not;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static uk.ac.man.cs.eventlite.testutil.MessageConverterUtil.getMessageConverters;

import uk.ac.man.cs.eventlite.EventLite;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EventLite.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class VenuesControllerApiTest {

	private MockMvc mvc;

	@Autowired
	private Filter springSecurityFilterChain;

	@Mock
	private EventService eventService;
	
	@Mock
	private VenueService venueService;

	@InjectMocks
	private EventsControllerApi eventsController;
	
	@InjectMocks
	private VenuesControllerApi venuesController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(venuesController).apply(springSecurity(springSecurityFilterChain))
				.setMessageConverters(getMessageConverters()).build();
	}

	@Test
	public void getIndexWhenNoVenues() throws Exception {
		when(venueService.findAll()).thenReturn(Collections.<Venue>emptyList());

        mvc.perform(get("/api/venues").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(handler().methodName("getAllVenues"))
                .andExpect(jsonPath("$.length()", equalTo(1)))
                .andExpect(jsonPath("$._links.self.href", endsWith("/api/venues")));

        verify(venueService).findAll();
	}

	@Test
	public void getIndexWithVenues() throws Exception {
		Venue v = new Venue();
		
		when(venueService.findAll()).thenReturn(Collections.<Venue>singletonList(v));

		mvc.perform(get("/api/venues").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(handler().methodName("getAllVenues"))
				.andExpect(jsonPath("$.length()", equalTo(2)))
				.andExpect(jsonPath("$._links.self.href", endsWith("/api/venues")))
				.andExpect(jsonPath("$._embedded.venues.length()", equalTo(1)))
				.andExpect(jsonPath("$._embedded.venues[0]._links.venue.href", not(empty())))
				.andExpect(jsonPath("$._embedded.venues[0]._links.venue.href", endsWith("venues/0/venue")));

		verify(venueService).findAll();
	}
	
	@Test
	public void getSingleVenue() throws Exception{
		Venue v = new Venue("Venue 1", 10, "1 Oxford Road", "M130RR");
		Event e = new Event();
		e.setId(0);
		e.setName("Event");
		e.setDate(LocalDate.now());
		e.setTime(LocalTime.now());
		e.setVenue(v);


		when(venueService.findOne((long) 0)).thenReturn(v);
		
		mvc.perform(get("/api/venues/0").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			
			.andExpect(handler().methodName("getVenue"))
			.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
			
			.andExpect(jsonPath("$.name", not(empty())))
			.andExpect(jsonPath("$.capacity", not(empty())))
			.andExpect(jsonPath("$.address", not(empty())))
			.andExpect(jsonPath("$.postcode", not(empty())))
			
			.andExpect(jsonPath("$._links.self.href", not(empty())))
			.andExpect(jsonPath("$._links.self.href", endsWith("/api/venues/0")))
			
			.andExpect(jsonPath("$._links.event.href", not(empty())))
			.andExpect(jsonPath("$._links.event.href", endsWith("/api/venues/0/event")))
			
			.andExpect(jsonPath("$._links.venue.href", not(empty())))
			.andExpect(jsonPath("$._links.venue.href", endsWith("/api/venues/0/venue")))
		
			.andExpect(jsonPath("$._links.next3events.href", not(empty())))
			.andExpect(jsonPath("$._links.next3events.href", endsWith("/api/venues/0/next3events")));
				
		verify(venueService).findOne((long) 0);

	}
}
