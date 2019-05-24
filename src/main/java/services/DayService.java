
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.DayRepository;
import domain.Actor;
import domain.Day;
import domain.Event;
import domain.Member;

@Service
@Transactional
public class DayService {

	// Managed repository
	@Autowired
	private DayRepository	dayRepository;

	// Supporting services
	@Autowired
	ActorService			actorService;

	@Autowired
	EventService			eventService;

	@Autowired
	MemberService			memberService;


	// Simple CRUD methods
	//R14.1
	public Day create() {
		Day result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		result = new Day();

		return result;
	}

	public Collection<Day> findAll() {
		Collection<Day> result;

		result = this.dayRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Day findOne(final int dayId) {
		Assert.isTrue(dayId != 0);

		Day result;

		result = this.dayRepository.findOne(dayId);
		Assert.notNull(result);

		return result;
	}

	//R14.1
	public Day save(final Day day, final Event event) {
		Assert.notNull(day);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Day result;

		Assert.isTrue(!event.getIsFinalMode(), "You can only save events that are not in final mode");

		final Date currentMoment = new Date(System.currentTimeMillis());
		if (day.getDate() != null)
			Assert.isTrue(day.getDate().compareTo(currentMoment) > 0, "The date of the day must be future");

		final Member memberOwner = this.memberService.findMemberByEventId(event.getId());
		Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");

		if (day.getId() == 0) {
			result = this.dayRepository.save(day);
			final Collection<Day> daysEvent = event.getDays();
			daysEvent.add(result);
			event.setDays(daysEvent);
			this.eventService.saveAuxiliar(event);
		} else
			result = this.dayRepository.save(day);

		return result;
	}

	public Day saveAuxiliar(final Day day) {
		Assert.notNull(day);

		Day result;

		result = this.dayRepository.save(day);

		return result;
	}

	//R14.1
	public void delete(final Day day, final Event event) {
		Assert.notNull(day);
		Assert.isTrue(day.getId() != 0);
		Assert.isTrue(this.dayRepository.exists(day.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Assert.isTrue(!event.getIsFinalMode(), "You can only delete events that are not in final mode");

		final Collection<Day> daysEvent = event.getDays();
		Assert.isTrue(daysEvent.size() != 1, "The event must be at least one day");

		daysEvent.remove(day);
		event.setDays(daysEvent);
		this.eventService.saveAuxiliar(event);

		this.dayRepository.delete(day);
	}

	// Other business methods
	//R14.1
	public Collection<Day> findDaysByEvent(final int eventId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Collection<Day> result;

		final Event event = this.eventService.findEventMemberLogged(eventId);

		result = event.getDays();
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Day reconstruct(final Day day, final BindingResult binding) {
		Day result;

		if (day.getId() == 0)
			result = day;
		else {
			final Day originalDay = this.dayRepository.findOne(day.getId());
			Assert.notNull(originalDay, "This entity does not exist");
			result = day;
		}

		this.validator.validate(result, binding);

		this.dayRepository.flush();

		return result;
	}

	public void flush() {
		this.dayRepository.flush();
	}

}
