/*
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.member;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.DayService;
import services.EventService;
import controllers.AbstractController;
import domain.Day;
import domain.Event;

@Controller
@RequestMapping("/day/member")
public class MemberDayController extends AbstractController {

	@Autowired
	DayService		dayService;

	@Autowired
	EventService	eventService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int eventId) {
		ModelAndView result;
		Collection<Day> days;
		final Event event = this.eventService.findOne(eventId);

		try {
			days = this.dayService.findDaysByEventMemberLogged(eventId);
			result = new ModelAndView("day/list");
			result.addObject("days", days);
			result.addObject("requestURI", "day/member/list.do");
			result.addObject("event", event);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(null, "hacking.logged.error", event);
			else
				result = this.createEditModelAndView(null, "commit.error", event);
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int eventId) {
		ModelAndView result;
		Day day;

		final Event event = this.eventService.findOne(eventId);
		final Collection<Day> days = this.dayService.findDaysByEventMemberLogged(eventId);
		day = this.dayService.create();

		result = this.createEditModelAndView(day, event);
		result.addObject("days", days);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int eventId, @RequestParam final int dayId) {
		ModelAndView result;
		Day day = null;
		final Event event = this.eventService.findOne(eventId);

		try {
			this.dayService.findDaysByEventMemberLogged(eventId);
			day = this.dayService.findOne(dayId);
			result = this.createEditModelAndView(day, event);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(day, "hacking.logged.error", event);
			else
				result = this.createEditModelAndView(day, "commit.error", event);
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Day day, final BindingResult binding, @RequestParam final int eventId) {
		ModelAndView result;

		final Event event = this.eventService.findOne(eventId);

		try {
			day = this.dayService.reconstruct(day, binding);
			this.dayService.findDaysByEventMemberLogged(eventId);
			final Collection<Day> days = this.dayService.findDaysByEventMemberLogged(eventId);
			if (binding.hasErrors())
				result = this.createEditModelAndView(day, event);
			else {
				this.dayService.save(day, event);
				result = new ModelAndView("redirect:/day/member/list.do?eventId=" + eventId);
			}
			result.addObject("days", days);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The date of the day must be future"))
				result = this.createEditModelAndView(day, "day.error.dateNotFuture", event);
			else if (oops.getMessage().equals("You can only save events that are not in final mode"))
				result = this.createEditModelAndView(day, "event.error.save.finalMode", event);
			else if (oops.getMessage().equals("The current day's date must be before than the next day's date"))
				result = this.createEditModelAndView(day, "day.error.save.dayBefore", event);
			else if (oops.getMessage().equals("The current day's date must be after than the previous day's date"))
				result = this.createEditModelAndView(day, "day.error.save.dayAfter", event);
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(day, "hacking.logged.error", event);
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error", event);
			else
				result = this.createEditModelAndView(day, "commit.error", event);
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int dayId, @RequestParam final int eventId) {
		ModelAndView result;

		final Event event = this.eventService.findOne(eventId);
		this.dayService.findDaysByEventMemberLogged(eventId);
		final Day day = this.dayService.findOne(dayId);

		try {
			this.dayService.delete(day, event);
			result = new ModelAndView("redirect:/day/member/list.do?eventId=" + eventId);
			result.addObject("event", event);

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(day, "hacking.logged.error", event);
			else if (oops.getMessage().equals("The event must be at least one day"))
				result = this.createEditModelAndView(day, "day.error.oneDay", event);
			else if (oops.getMessage().equals("You can only delete events that are not in final mode"))
				result = this.createEditModelAndView(day, "event.error.delete.finalMode", event);
			else
				result = this.createEditModelAndView(day, "commit.error", event);
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Day day, final Event event) {
		ModelAndView result;
		result = this.createEditModelAndView(day, null, event);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Day day, final String message, final Event event) {
		ModelAndView result;

		if (day == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (day.getId() == 0) {
			result = new ModelAndView("day/create");
			final List<Day> days = new ArrayList<>(event.getDays());
			final Day lastDay = days.get(days.size() - 1);
			final SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
			final Calendar cal = Calendar.getInstance();
			cal.setTime(lastDay.getDate());
			cal.add(Calendar.DATE, 1);
			result.addObject("lastDate", formatDate.format(cal.getTime()));
		} else
			result = new ModelAndView("day/edit");

		result.addObject("event", event);
		result.addObject("day", day);
		result.addObject("actionURL", "day/member/edit.do");
		result.addObject("message", message);

		return result;
	}
}
