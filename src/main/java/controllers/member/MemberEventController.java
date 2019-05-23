/*
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.member;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;
import services.ActorService;
import services.DayService;
import services.EventService;
import controllers.AbstractController;
import domain.Activity;
import domain.Actor;
import domain.Day;
import domain.Event;
import domain.Member;

@Controller
@RequestMapping("/event/member")
public class MemberEventController extends AbstractController {

	@Autowired
	EventService	eventService;

	@Autowired
	DayService		dayService;

	@Autowired
	ActivityService	activityService;

	@Autowired
	ActorService	actorService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Event> events = null;
		final Day day = null;

		try {
			events = this.eventService.findEventsByMemberLogged();
			result = this.createEditModelAndView(events, day);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You need to belong to an association to manage events"))
				result = this.createEditModelAndView(events, "event.notBelongs.error", null);
			else
				result = this.createEditModelAndView(events, "commit.error", null);
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Event event = null;
		Day day = null;

		try {
			event = this.eventService.create();
			day = this.dayService.create();
			result = this.createEditModelAndView(event, day);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You need to belong to an association to manage events"))
				result = this.createEditModelAndView(event, "event.notBelongs.error", null);
			else
				result = this.createEditModelAndView(event, "commit.error", null);
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int eventId) {
		ModelAndView result;
		Event event = null;

		try {
			event = this.eventService.findEventMemberLogged(eventId);
			result = this.createEditModelAndView(event, null);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(event, "hacking.logged.error", null);
			else
				result = this.createEditModelAndView(event, "commit.error", null);
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Event event, final BindingResult binding, final @Valid Day day, final BindingResult binding2) {
		ModelAndView result;

		try {
			if (event.getId() == 0) {
				final Date currentMoment = new Date(System.currentTimeMillis());
				if (day.getDate() != null)
					Assert.isTrue(day.getDate().compareTo(currentMoment) > 0, "The date of the day must be future");
				final Collection<Day> days = new HashSet<>();
				event.setDays(days);
				event.getDays().add(day);
			}

			event = this.eventService.reconstruct(event, binding);

			if (event.getId() == 0) {
				if (binding.hasErrors() || binding2.hasErrors())
					result = this.createEditModelAndView(event, day);
				else {
					this.eventService.save(event);
					result = new ModelAndView("redirect:/event/member/list.do");
				}

			} else if (binding.hasErrors())
				result = this.createEditModelAndView(event, day);
			else {
				this.eventService.save(event);
				result = new ModelAndView("redirect:/event/member/list.do");
			}

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You need to belong to an association to manage events"))
				result = this.createEditModelAndView(event, "event.notBelongs.error", day);
			else if (oops.getMessage().equals("You can only save events that are not in final mode"))
				result = this.createEditModelAndView(event, "event.error.save.finalMode", day);
			else if (oops.getMessage().equals("Some of the selected activities do not belong to your association"))
				result = this.createEditModelAndView(event, "event.error.activitiesNotSameAssociation", day);
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(event, "hacking.logged.error", day);
			else if (oops.getMessage().equals("The date of the day must be future"))
				result = this.createEditModelAndView(event, "event.error.dayNotFuture", day);
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(event, "hacking.notExist.error", day);
			else
				result = this.createEditModelAndView(event, "commit.error", day);
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int eventId) {
		ModelAndView result;

		Event event = null;

		try {
			event = this.eventService.findEventMemberLogged(eventId);
			this.eventService.delete(event);
			result = new ModelAndView("redirect:/event/member/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can only delete events that are not in final mode"))
				result = this.createEditModelAndView(event, "event.error.delete.finalMode", null);
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(event, "hacking.logged.error", null);
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(event, "hacking.notExist.error", null);
			else
				result = this.createEditModelAndView(event, "commit.error", null);
		}

		return result;
	}

	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public ModelAndView changeFinalMode(@RequestParam final int eventId) {
		ModelAndView result;

		Event event = null;

		try {
			event = this.eventService.findEventMemberLogged(eventId);
			this.eventService.changeFinalMode(event);
			result = new ModelAndView("redirect:/event/member/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This event is already in final mode"))
				result = this.createEditModelAndView(event, "event.error.change.finalMode", null);
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(event, "hacking.logged.error", null);
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(event, "hacking.notExist.error", null);
			else
				result = this.createEditModelAndView(event, "commit.error", null);
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Event event, final Day day) {
		ModelAndView result;
		result = this.createEditModelAndView(event, null, day);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Event event, final String message, final Day day) {
		ModelAndView result;

		if (event == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			if (event.getId() == 0)
				result = new ModelAndView("event/create");
			else
				result = new ModelAndView("event/edit");

			final Actor actorLogged = this.actorService.findActorLogged();
			if (actorLogged != null) {
				final Member memberLogged = (Member) actorLogged;
				final Collection<Activity> activities = this.activityService.findActivitiesFinalModeByAssociationId(memberLogged.getAssociation().getId());
				result.addObject("activities", activities);
			}
		}

		result.addObject("event", event);
		result.addObject("day", day);
		result.addObject("actionURL", "event/member/edit.do");
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Event> events, final Day day) {
		ModelAndView result;
		result = this.createEditModelAndView(events, null, day);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Event> events, final String message, final Day day) {
		ModelAndView result;

		if (events == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("event/list");

		result.addObject("events", events);
		result.addObject("day", day);
		result.addObject("requestURI", "event/member/list.do");
		result.addObject("message", message);

		return result;
	}
}
