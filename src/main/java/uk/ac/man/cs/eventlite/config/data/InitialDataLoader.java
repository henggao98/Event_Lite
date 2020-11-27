package uk.ac.man.cs.eventlite.config.data;

import java.time.LocalDate;
import java.time.LocalTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import uk.ac.man.cs.eventlite.config.MapGeocoding;
import uk.ac.man.cs.eventlite.dao.EventService;
import uk.ac.man.cs.eventlite.dao.VenueService;
import uk.ac.man.cs.eventlite.entities.Venue;
import uk.ac.man.cs.eventlite.entities.Event;

@Component
@Profile({ "default", "test" })
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

	private final static Logger log = LoggerFactory.getLogger(InitialDataLoader.class);

	@Autowired
	private EventService eventService;

	@Autowired
	private VenueService venueService;

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (venueService.count() > 0) {//// skip if venue list is populated
			log.info("Database already populated. Skipping data initialization.");
		} else {// fill the venue list with an initial venue.
			Venue venue1 = new Venue("Venue 1", 10, "1 Oxford Road", "M130RR");
			MapGeocoding.setLonAndLat(venue1);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			venueService.save(venue1);

			Venue venue2 = new Venue("Venue 2", 10, "Rusholme", "M145RR");
			MapGeocoding.setLonAndLat(venue2);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			venueService.save(venue2);	
			
			Venue venue3 = new Venue("Venue 3", 10, "Brighton", "BN13BG");
			MapGeocoding.setLonAndLat(venue3);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			venueService.save(venue3);	
			
			Venue venue4 = new Venue("Venue 4", 10, "Crawcrook", "NE40 4UN");
			MapGeocoding.setLonAndLat(venue4);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			venueService.save(venue4);
			
		}
		if (eventService.count() > 0) {// skip if event list ok.
			log.info("Database already populated. Skipping data initialization.");
		} else {// fill the event list with some initial events

			 Event event1 = new Event(); 
			 event1.setName("Event 1");
			 event1.setDate(LocalDate.of(2020, 07, 20)); 
			 event1.setTime(LocalTime.of(12,30, 0));
			 event1.setVenue(venueService.findFirstByNameOrderByNameAsc("Venue 1"));
			 event1.setDescription("a");

			 MapGeocoding.setLonAndLat(event1);
			 try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 eventService.save(event1);


			Event event2 = new Event();
			event2.setName("Event 2");
			event2.setDate(LocalDate.of(2020, 06, 21));
			event2.setTime(LocalTime.of(14, 30, 0));
			event2.setVenue(venueService.findFirstByNameOrderByNameAsc("Venue 2"));
			event2.setDescription("b");

			MapGeocoding.setLonAndLat(event2);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			eventService.save(event2);

			Event event3 = new Event(); 
			event3.setName("Event 3");
			event3.setDate(LocalDate.of(2021, 02, 22)); 
			event3.setTime(LocalTime.of(12,30, 0));

			event3.setVenue(venueService.findFirstByNameOrderByNameAsc("Venue 3"));
			event3.setDescription("c");
			MapGeocoding.setLonAndLat(event3);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			eventService.save(event3);
			
			Event event4 = new Event(); 
			event4.setName("Event 4");
			event4.setDate(LocalDate.of(2020, 12, 22)); 
			event4.setTime(LocalTime.of(12,35, 0));

			event4.setVenue(venueService.findFirstByNameOrderByNameAsc("Venue 2"));
			event4.setDescription("d");
			MapGeocoding.setLonAndLat(event4);
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			eventService.save(event4);
			
			Event event5 = new Event(); 
			event5.setName("Event 5");
			event5.setDate(LocalDate.of(2022, 02, 22)); 
			event5.setTime(LocalTime.of(12,35, 0));
			event5.setVenue(venueService.findFirstByNameOrderByNameAsc("Venue 3"));
			event5.setDescription("e");
			eventService.save(event5);
			
			Event event6 = new Event(); 
			event6.setName("Event 6");
			event6.setDate(LocalDate.of(2020, 07, 22)); 
			event6.setTime(LocalTime.of(12,41, 0));
			event6.setVenue(venueService.findFirstByNameOrderByNameAsc("Venue 4"));
			event6.setDescription("f");
			eventService.save(event6);
			
			Event event7 = new Event(); 
			event7.setName("Event 7");
			event7.setDate(LocalDate.of(2028, 02, 22)); 
			event7.setTime(LocalTime.of(12,35, 0));
			event7.setVenue(venueService.findFirstByNameOrderByNameAsc("Venue 1"));
			event7.setDescription("g");
			eventService.save(event7);
			
			Event event8 = new Event(); 
			event8.setName("Event 8");
			event8.setDate(LocalDate.of(2022, 02, 22)); 
			event8.setTime(LocalTime.of(12,35, 0));
			event8.setVenue(venueService.findFirstByNameOrderByNameAsc("Venue 1"));
			event8.setDescription("h");
			eventService.save(event8);
			
			Event event9 = new Event(); 
			event9.setName("Event 9");
			event9.setDate(LocalDate.of(2020, 04, 26)); 
			event9.setTime(LocalTime.of(12,35, 0));
			event9.setVenue(venueService.findFirstByNameOrderByNameAsc("Venue 1"));
			event9.setDescription("i");
			eventService.save(event9);
			
		}
	}
}
