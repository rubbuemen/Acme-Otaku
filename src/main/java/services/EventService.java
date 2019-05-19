
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EventRepository;
import domain.Event;

@Service
@Transactional
public class EventService {

	// Managed repository
	@Autowired
	private EventRepository	eventRepository;


	// Supporting services

	// Simple CRUD methods
	public Event create() {
		Event result;

		result = new Event();
		return result;
	}

	public Collection<Event> findAll() {
		Collection<Event> result;

		result = this.eventRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Event findOne(final int eventId) {
		Assert.isTrue(eventId != 0);

		Event result;

		result = this.eventRepository.findOne(eventId);
		Assert.notNull(result);

		return result;
	}

	public Event save(final Event event) {
		Assert.notNull(event);

		Event result;

		if (event.getId() == 0)
			result = this.eventRepository.save(event);
		else
			result = this.eventRepository.save(event);

		return result;
	}

	public void delete(final Event event) {
		Assert.notNull(event);
		Assert.isTrue(event.getId() != 0);
		Assert.isTrue(this.eventRepository.exists(event.getId()));

		this.eventRepository.delete(event);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Event reconstruct(final Event event, final BindingResult binding) {
		Event result;

		if (event.getId() == 0)
			result = event;
		else {
			final Event originalEvent = this.eventRepository.findOne(event.getId());
			Assert.notNull(originalEvent, "This entity does not exist");
			result = event;
		}

		this.validator.validate(result, binding);

		this.eventRepository.flush();

		return result;
	}

	public void flush() {
		this.eventRepository.flush();
	}

}
