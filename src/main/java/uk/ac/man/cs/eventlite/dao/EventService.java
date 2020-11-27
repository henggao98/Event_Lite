package uk.ac.man.cs.eventlite.dao;

import java.util.Optional;

import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

public interface EventService {

	public long count();

	public Iterable<Event> findAll();
	
	public <S extends Event> S save(S event);
	
	public Event findOne(Long id);

	Optional<Event> findById(Long id);
	
	public Iterable<Event> findByVenue(Venue v);
	
	public void deleteById(long id);

    public 	Iterable<Event> findByName(String name);
}
