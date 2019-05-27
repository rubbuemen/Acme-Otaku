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
import java.util.Collections;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.HeadquarterService;
import services.MeetingService;
import controllers.AbstractController;
import domain.Headquarter;
import domain.Meeting;

@Controller
@RequestMapping("/meeting/member")
public class MemberMeetingController extends AbstractController {

	@Autowired
	MeetingService		meetingService;

	@Autowired
	HeadquarterService	headquarterService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Meeting> meetings = null;

		try {
			meetings = this.meetingService.findMeetingsByMemberLogged();
			result = this.createEditModelAndView(meetings);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association for manage meetings"))
				result = this.createEditModelAndView(meetings, "meeting.notPresident.error");
			else
				result = this.createEditModelAndView(meetings, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Meeting meeting = null;

		try {
			meeting = this.meetingService.create();
			result = this.createEditModelAndView(meeting);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association for manage meetings"))
				result = this.createEditModelAndView(meeting, "meeting.notPresident.error");
			else
				result = this.createEditModelAndView(meeting, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int meetingId) {
		ModelAndView result;
		Meeting meeting = null;

		try {
			meeting = this.meetingService.findMeetingMemberLogged(meetingId);
			result = this.createEditModelAndView(meeting, null);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(meeting, "hacking.logged.error");
			else
				result = this.createEditModelAndView(meeting, "commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Meeting meeting, final BindingResult binding) {
		ModelAndView result;

		try {
			meeting = this.meetingService.reconstruct(meeting, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(meeting);
			else {
				this.meetingService.save(meeting);
				result = new ModelAndView("redirect:/meeting/member/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association for manage meetings"))
				result = this.createEditModelAndView(meeting, "meeting.notPresident.error");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(meeting, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(meeting, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(meeting, "commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int meetingId) {
		ModelAndView result;

		Meeting meeting = null;

		try {
			meeting = this.meetingService.findMeetingMemberLogged(meetingId);
			this.meetingService.delete(meeting);
			result = new ModelAndView("redirect:/meeting/member/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association for manage meetings"))
				result = this.createEditModelAndView(meeting, "meeting.notPresident.error");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(meeting, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(meeting, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(meeting, "commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Meeting meeting) {
		ModelAndView result;
		result = this.createEditModelAndView(meeting, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Meeting meeting, final String message) {
		ModelAndView result;

		if (meeting == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			if (meeting.getId() == 0)
				result = new ModelAndView("meeting/create");
			else
				result = new ModelAndView("meeting/edit");

			final Collection<String> typeList = new HashSet<>();
			Collections.addAll(typeList, "PUBLIC", "PRIVATE");
			final Collection<Headquarter> headquarters = this.headquarterService.findHeadquartersByMemberLogged();
			result.addObject("headquarters", headquarters);
			result.addObject("typeList", typeList);
		}

		result.addObject("meeting", meeting);
		result.addObject("actionURL", "meeting/member/edit.do");
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Meeting> meetings) {
		ModelAndView result;
		result = this.createEditModelAndView(meetings, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Meeting> meetings, final String message) {
		ModelAndView result;

		if (meetings == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("meeting/list");

		result.addObject("meetings", meetings);
		result.addObject("requestURI", "meeting/member/list.do");
		result.addObject("message", message);

		return result;
	}
}
