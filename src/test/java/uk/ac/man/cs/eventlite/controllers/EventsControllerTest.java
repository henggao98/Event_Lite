package uk.ac.man.cs.eventlite.controllers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import uk.ac.man.cs.eventlite.config.Security;
import javax.servlet.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
public class EventsControllerTest {

	private MockMvc mvc;

	@Autowired
	private Filter springSecurityFilterChain;


	@Mock
	private Event event;

	@Mock
	private Venue venue;

	@Mock
	private EventService eventService;

	@Mock
	private VenueService venueService;

	@InjectMocks
	private EventsController eventsController;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		mvc = MockMvcBuilders.standaloneSetup(eventsController).apply(springSecurity(springSecurityFilterChain))
				.build();
	}

	@Test
	public void getIndexWhenNoEvents() throws Exception {
		when(eventService.findAll()).thenReturn(Collections.<Event> emptyList());
		//when(venueService.findAll()).thenReturn(Collections.<Venue> emptyList());

		mvc.perform(get("/events").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/index")).andExpect(handler().methodName("getAllEvents"));

		verify(eventService).findAll();
		//verify(venueService).findAll();
		verifyZeroInteractions(event);
		verifyZeroInteractions(venue);
	}

	@Test
	public void getIndexWithEvents() throws Exception {
		when(eventService.findAll()).thenReturn(Collections.<Event> singletonList(event));
		//when(venueService.findAll()).thenReturn(Collections.<Venue> singletonList(venue));
		
		mvc.perform(get("/events").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
				.andExpect(view().name("events/index")).andExpect(handler().methodName("getAllEvents"));
	
		verify(eventService).findAll();
		//verify(venueService).findAll();

		verifyZeroInteractions(venue);
	}
	
	@Test
	public void postNewEvent() throws Exception {
		ArgumentCaptor<Event> arg = ArgumentCaptor.forClass(Event.class);

		mvc.perform(MockMvcRequestBuilders.post("/events").with(user("Rob").roles(Security.ADMIN_ROLE))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", "0")
				.param("name", "Event Test")
				.param("date", (LocalDate.now().plus(1, ChronoUnit.DAYS)).toString())
				.param("time", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))
				.param("venue.id", "0")
				.param("description", "Test")
				.accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(model().hasNoErrors())
				.andExpect(view().name("redirect:/events"))				
				.andExpect(status().isFound())
				.andExpect(handler().methodName("addEvent"))
				.andExpect(flash().attributeExists("ok_message"));
	
	
		verify(eventService).save(arg.capture());
		assertThat(event.getId(), equalTo(arg.getValue().getId()));
		assertThat("Event Test", equalTo(arg.getValue().getName()));
		assertThat((LocalDate.now().plus(1, ChronoUnit.DAYS)), equalTo(arg.getValue().getDate()));
		assertThat(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), equalTo(arg.getValue().getTime().toString()));
		assertThat(venue.getId(), equalTo(arg.getValue().getVenue().getId()));
		assertThat("Test", equalTo(arg.getValue().getDescription()));
	}
	


	@Test
	public void postAddEventWithErrors() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/events").with(user("Rob").roles(Security.ADMIN_ROLE))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("id", "0")
				.param("name", "")
				.param("date", (LocalDate.now().minus(1, ChronoUnit.DAYS)).toString())
				.param("time", LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))
				.param("venue.id", "")
				.param("description", "Lorem ipsum dolor sit amet, consectetur adipiscing elit. "
						+ "Praesent venenatis, libero ac ultricies efficitur, massa tellus blandit erat,"
						+ " nec convallis enim leo vitae nisl. Nunc in lobortis turpis, in blandit dolor."
						+ " Maecenas eu arcu sollicitudin, fermentum mi quis, cursus ipsum. Praesent tempor "
						+ "euismod nisi, at dapibus arcu porta in. Sed semper diam urna, ut viverra dolor viverra at. "
						+ "Praesent in enim vel odio iaculis consectetur. Quisque malesuada libero posuere odio blandit"
						+ " rhoncus. In sed neque quam. Nam gravida enim ut mauris varius, in sodales augue tempor.")
				.accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isOk())
				.andExpect(view().name("events/addEvent"))
				.andExpect(model().attributeHasFieldErrors("event", "date"))
				.andExpect(model().attributeHasFieldErrors("event", "name"))
				.andExpect(model().attributeHasFieldErrors("event", "venue.id"))
				.andExpect(model().attributeHasFieldErrors("event", "description"))
				.andExpect(handler().methodName("addEvent"))
				.andExpect(flash().attributeCount(0));

		verify(eventService, never()).save(event);
	}

	@Test
	public void getEventsByName() throws Exception {
		when(eventService.findByName("Event 1")).thenReturn(Collections.<Event> singletonList(event));

		mvc.perform(get("/events/index/search?sname=Event 1").accept(MediaType.TEXT_HTML)).andExpect(status().isOk())
		.andExpect(view().name("events/index")).andExpect(handler().methodName("getNameSpecifiedEvents"));
		
		verify(eventService).findByName("Event 1");
	}
	
	@Test
	public void deleteEvent() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders.delete("/events/delete/{id}", event.getId()).with(user("Rob").roles(Security.ADMIN_ROLE))
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.accept(MediaType.TEXT_HTML).with(csrf()))
				.andExpect(status().isFound())
				.andExpect(view().name("redirect:/events"))
				.andExpect(handler().methodName("delete"));
		
		verify(eventService).deleteById(event.getId());
	}
}