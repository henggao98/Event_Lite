package uk.ac.man.cs.eventlite.dao;

import java.util.Optional;

import uk.ac.man.cs.eventlite.entities.Venue;

public interface VenueService {

	public long count();

	public Iterable<Venue> findAll();
	
	public Venue save(Venue venue);
	
	public Venue findFirstByNameOrderByNameAsc(String name);
	
	public Venue findOne(Long id);
	
	Optional<Venue> findById(Long id);
	
	public Iterable<Venue> findByName(String name);
	
}
