
package services;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
	public Day save(Day day, final Event event) {
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
			final List<Day> daysEvent = new ArrayList<>(event.getDays());
			final Day lastDay = daysEvent.get(daysEvent.size() - 1);
			final Calendar cal = Calendar.getInstance();
			cal.setTime(lastDay.getDate());
			final int dayCalendar = cal.get(Calendar.DATE);
			final int monthCalendar = cal.get(Calendar.MONTH) + 1;
			if (dayCalendar == 31 && monthCalendar == 12)
				cal.add(Calendar.YEAR, 1);
			cal.add(Calendar.DATE, 1);
			day.setDate(cal.getTime());
			result = this.dayRepository.save(day);
			daysEvent.add(result);
			event.setDays(daysEvent);
			this.eventService.saveAuxiliar(event);
		} else {
			final List<Day> daysEvent = new ArrayList<>(event.getDays());
			if (daysEvent.size() > 1)
				if (daysEvent.get(0).equals(day)) {
					final Day nextDay = daysEvent.get(1);
					if (day.getDate() != null)
						Assert.isTrue(day.getDate().compareTo(nextDay.getDate()) < 0, "The current day's date must be before than the next day's date");
					result = this.dayRepository.save(day);
					if (day.getDate() != null) {
						daysEvent.remove(day);
						for (final Day d : daysEvent) {
							final Calendar cal = Calendar.getInstance();
							cal.setTime(day.getDate());
							cal.add(Calendar.DATE, 1);
							d.setDate(cal.getTime());
							this.dayRepository.save(d);
							day = d;
						}
					}
				} else if (daysEvent.get(daysEvent.size() - 1).equals(day)) {
					final Day previousDay = daysEvent.get(daysEvent.size() - 2);
					if (day.getDate() != null)
						Assert.isTrue(day.getDate().compareTo(previousDay.getDate()) > 0, "The current day's date must be after than the previous day's date");
					result = this.dayRepository.save(day);
					if (day.getDate() != null) {
						daysEvent.remove(day);
						Collections.reverse(daysEvent);
						for (final Day d : daysEvent) {
							final Calendar cal = Calendar.getInstance();
							cal.setTime(day.getDate());
							cal.add(Calendar.DATE, -1);
							d.setDate(cal.getTime());
							this.dayRepository.save(d);
							day = d;
						}
					}
				} else {
					final int actualIndex = daysEvent.indexOf(day);
					final Day nextDay = daysEvent.get(actualIndex + 1);
					final Day previousDay = daysEvent.get(actualIndex - 1);
					if (day.getDate() != null) {
						Assert.isTrue(day.getDate().compareTo(previousDay.getDate()) > 0, "The current day's date must be after than the previous day's date");
						Assert.isTrue(day.getDate().compareTo(nextDay.getDate()) < 0, "The current day's date must be before than the next day's date");
					}
					result = this.dayRepository.save(day);
				}
			else
				result = this.dayRepository.save(day);
		}

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

		final Collection<Day> daysEventt = event.getDays();
		Assert.isTrue(daysEventt.size() != 1, "The event must be at least one day");

		List<Day> daysEvent = new ArrayList<>(event.getDays());

		if (!daysEvent.get(0).equals(day) && !daysEvent.get(daysEvent.size() - 1).equals(day)) {
			daysEvent = daysEvent.subList(daysEvent.indexOf(day), daysEvent.size());
			daysEvent.remove(day);
			for (final Day d : daysEvent) {
				final Calendar cal = Calendar.getInstance();
				cal.setTime(d.getDate());
				cal.add(Calendar.DATE, -1);
				d.setDate(cal.getTime());
				this.dayRepository.save(d);
			}
		}

		daysEventt.remove(day);
		event.setDays(daysEventt);
		this.eventService.saveAuxiliar(event);

		this.dayRepository.delete(day);
	}

	// Other business methods
	//R14.1
	public Collection<Day> findDaysByEventMemberLogged(final int eventId) {
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
