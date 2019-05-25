
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EventRepository;
import domain.Activity;
import domain.Actor;
import domain.Day;
import domain.Event;
import domain.Member;
import domain.Sponsorship;

@Service
@Transactional
public class EventService {

	// Managed repository
	@Autowired
	private EventRepository	eventRepository;

	// Supporting services
	@Autowired
	private ActorService	actorService;

	@Autowired
	private MemberService	memberService;

	@Autowired
	private ActivityService	ativityService;

	@Autowired
	private DayService		dayService;


	// Simple CRUD methods
	//R14.1
	public Event create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;
		Assert.notNull(memberLogged.getAssociation(), "You need to belong to an association to manage events");

		Event result;

		result = new Event();
		final Collection<Sponsorship> sponsorships = new HashSet<>();
		final Collection<Activity> activities = new HashSet<>();
		final Activity activity = new Activity(); // Ghost Activity because it is mandatory to have at least one
		activities.add(activity);
		final Collection<Day> days = new HashSet<>();
		final Day day = new Day(); // Ghost Day because it is mandatory to have at least one
		days.add(day);

		final Date moment = new Date(System.currentTimeMillis() - 1);

		result.setSponsorships(sponsorships);
		result.setActivities(activities);
		result.setDays(days);
		result.setMoment(moment);
		result.setIsFinalMode(false);

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

	//R14.1
	public Event save(final Event event) {
		Assert.notNull(event);
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Event result;

		final Member memberLogged = (Member) actorLogged;
		Assert.notNull(memberLogged.getAssociation(), "You need to belong to an association to manage events");

		Assert.isTrue(!event.getIsFinalMode(), "You can only save events that are not in final mode");

		final Collection<Activity> activitiesAssociationMember = this.ativityService.findActivitiesFinalModeByAssociationId(memberLogged.getAssociation().getId());
		final Collection<Activity> activitiesEvent = event.getActivities();
		Assert.isTrue(activitiesAssociationMember.containsAll(activitiesEvent), "Some of the selected activities do not belong to your association");

		if (event.getId() == 0) {
			final Date moment = new Date(System.currentTimeMillis() - 1);
			event.setMoment(moment);
			Day day = event.getDays().iterator().next();
			event.getDays().remove(day);
			day = this.dayService.saveAuxiliar(day);
			event.getDays().add(day);
			result = this.eventRepository.save(event);
			final Collection<Event> eventsMemberLogged = memberLogged.getEvents();
			eventsMemberLogged.add(result);
			memberLogged.setEvents(eventsMemberLogged);
			this.memberService.saveAuxiliar(memberLogged);
		} else {
			final Member memberOwner = this.memberService.findMemberByEventId(event.getId());
			Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");
			result = this.eventRepository.save(event);
		}

		return result;
	}

	public Event saveAuxiliar(final Event event) {
		Assert.notNull(event);

		Event result;

		result = this.eventRepository.save(event);

		return result;
	}

	//R14.1
	public void delete(final Event event) {
		Assert.notNull(event);
		Assert.isTrue(event.getId() != 0);
		Assert.isTrue(this.eventRepository.exists(event.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberOwner = this.memberService.findMemberByEventId(event.getId());
		Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");

		Assert.isTrue(!event.getIsFinalMode(), "You can only delete events that are not in final mode");

		final Member memberLogged = (Member) actorLogged;

		final Collection<Event> eventsActorLogged = memberLogged.getEvents();
		eventsActorLogged.remove(event);
		memberLogged.setEvents(eventsActorLogged);
		this.memberService.saveAuxiliar(memberLogged);

		this.eventRepository.delete(event);
	}

	public void deleteAuxiliar(final Event event) {
		Assert.notNull(event);
		Assert.isTrue(event.getId() != 0);
		Assert.isTrue(this.eventRepository.exists(event.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		final Member memberLogged = (Member) actorLogged;

		final Collection<Event> eventsActorLogged = memberLogged.getEvents();
		eventsActorLogged.remove(event);
		memberLogged.setEvents(eventsActorLogged);
		this.memberService.saveAuxiliar(memberLogged);

		this.eventRepository.delete(event);
	}

	// Other business methods
	//R14.1
	public Collection<Event> findEventsByMemberLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Collection<Event> result;

		final Member memberLogged = (Member) actorLogged;
		Assert.notNull(memberLogged.getAssociation(), "You need to belong to an association to manage events");

		result = this.eventRepository.findEventsByMemberId(memberLogged.getId());
		Assert.notNull(result);

		return result;
	}

	//R14.1
	public Event changeFinalMode(final Event event) {
		Assert.notNull(event);
		Assert.isTrue(event.getId() != 0);
		Assert.isTrue(this.eventRepository.exists(event.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Event result;

		final Member memberOwner = this.memberService.findMemberByEventId(event.getId());
		Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");

		Assert.isTrue(!event.getIsFinalMode(), "This event is already in final mode");
		event.setIsFinalMode(true);

		result = this.eventRepository.save(event);

		return result;
	}

	public Event findEventMemberLogged(final int eventId) {
		Assert.isTrue(eventId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberOwner = this.memberService.findMemberByEventId(eventId);
		Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");

		Event result;

		result = this.eventRepository.findOne(eventId);
		Assert.notNull(result);

		return result;
	}

	//R15.1
	public Collection<Event> findEventsFinalModeNotFinished() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		Collection<Event> result;

		result = this.eventRepository.findEventsFinalModeNotFinished();
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Event reconstruct(final Event event, final BindingResult binding) {
		Event result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		final Date moment = new Date(System.currentTimeMillis() - 1);

		if (event.getActivities() == null || event.getActivities().contains(null)) {
			final Collection<Activity> activities = new HashSet<>();
			event.setActivities(activities);
		}

		if (event.getId() == 0) {
			final Collection<Sponsorship> sponsorships = new HashSet<>();
			event.setSponsorships(sponsorships);
			event.setIsFinalMode(false);
			event.setMoment(moment);
			result = event;
		} else {
			final Event originalevent = this.eventRepository.findOne(event.getId());
			Assert.notNull(originalevent, "This entity does not exist");
			result = event;
			result.setSponsorships(originalevent.getSponsorships());
			result.setDays(originalevent.getDays());
			result.setIsFinalMode(originalevent.getIsFinalMode());
			result.setMoment(originalevent.getMoment());
		}

		this.validator.validate(result, binding);

		this.eventRepository.flush();

		return result;
	}

	public void flush() {
		this.eventRepository.flush();
	}

}
