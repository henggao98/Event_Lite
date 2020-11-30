package uk.ac.man.cs.eventlite.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.ac.man.cs.eventlite.entities.Event;
import uk.ac.man.cs.eventlite.entities.Venue;

import java.util.Optional;

//import java.io.InputStream;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

@Service
public class EventServiceImpl implements EventService {


	
	@Autowired 
	private EventRepository eventRepository;

	@Override
	public long count() {
		return eventRepository.count();
	}

	@Override
	public Iterable<Event> findAll() {
		return eventRepository.findAllByOrderByDateAscTimeAsc();
	}
	
	@Override
	public <S extends Event> S save(S entity){
		return eventRepository.save(entity);
	}
	
	@Override
	public Optional<Event> findById(Long id) {
		return(eventRepository.findById(id));
	}

	@Override
	public Event findOne(Long id) {
		return findById(id).orElse(null);
	}

    public Iterable<Event> findByVenue(Venue v) {
    	return(eventRepository.findByVenue(v));
    }
    
    @Override
	public void deleteById(long id) {
		eventRepository.deleteById(id);
	}
	
	@Override
	public Iterable<Event> findByName(String name) {
		return eventRepository.findByNameLikeOrderByDateAscTimeAsc(name);
	}
}
