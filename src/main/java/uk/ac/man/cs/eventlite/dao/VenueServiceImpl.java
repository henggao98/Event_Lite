package uk.ac.man.cs.eventlite.dao;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import uk.ac.man.cs.eventlite.entities.Venue;

@Service
public class VenueServiceImpl implements VenueService {

	@Autowired
	private VenueRepository venueRepository;
	
	//private final static Logger log = LoggerFactory.getLogger(VenueServiceImpl.class);

	//private final static String DATA = "data/venues.json";

	@Override
	public long count() {
		return venueRepository.count();
	}

	@Override
	public Iterable<Venue> findAll() {
		return venueRepository.findAll();
	}
	
	@Override
	public Venue findFirstByNameOrderByNameAsc(String name) {
		return venueRepository.findFirstByNameOrderByNameAsc(name);
	}	

	@Override
	public Venue save(Venue venue) {
		return venueRepository.save(venue);
	}
	
	@Override
	public Optional<Venue> findById(Long id) {
		return(venueRepository.findById(id));
	}
	
	@Override
	public Venue findOne(Long id) {
		return findById(id).orElse(null);
	}
	
	
	@Override
	public Iterable<Venue> findByName(String name) {
		return venueRepository.findByNameOrderByNameAsc(name);
	}	

}
