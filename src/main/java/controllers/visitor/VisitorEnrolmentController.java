/*
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.visitor;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;
import services.ActorService;
import services.EnrolmentService;
import controllers.AbstractController;
import domain.Activity;
import domain.Enrolment;

@Controller
@RequestMapping("/enrolment/visitor")
public class VisitorEnrolmentController extends AbstractController {

	@Autowired
	EnrolmentService	enrolmentService;

	@Autowired
	ActivityService		activityService;

	@Autowired
	ActorService		actorService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Enrolment> enrolments;

		enrolments = this.enrolmentService.findEnrolmentsByVisitorLogged();

		result = new ModelAndView("enrolment/list");

		final Date currentMoment = new Date(System.currentTimeMillis());

		result.addObject("enrolments", enrolments);
		result.addObject("currentMoment", currentMoment);
		result.addObject("requestURI", "enrolment/visitor/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Enrolment enrolment;

		enrolment = this.enrolmentService.create();

		result = this.createEditModelAndView(enrolment);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Enrolment enrolment, final BindingResult binding) {
		ModelAndView result;

		try {
			enrolment = this.enrolmentService.reconstruct(enrolment, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(enrolment);
			else {
				this.enrolmentService.save(enrolment);
				result = new ModelAndView("redirect:/enrolment/visitor/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You already have an enrolment for this activity pending or accepted"))
				result = this.createEditModelAndView(enrolment, "enrolment.alreadyEnrolled.error");
			else if (oops.getMessage().equals("This activity is not available for enrolment"))
				result = this.createEditModelAndView(enrolment, "enrolment.notAvailable.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(enrolment, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(enrolment, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/cancel", method = RequestMethod.GET)
	public ModelAndView cancelEnrolment(@RequestParam final int enrolmentId) {
		ModelAndView result;

		final Enrolment enrolment = this.enrolmentService.findOne(enrolmentId);
		Collection<Enrolment> enrolments = null;

		try {
			enrolments = this.enrolmentService.findEnrolmentsByVisitorLogged();
			this.enrolmentService.cancelEnrolment(enrolment);
			result = new ModelAndView("redirect:/enrolment/visitor/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The status of this enrolment is not 'pending' or 'accepted'"))
				result = this.createEditModelAndView(enrolments, "enrolment.notPendingOrAccepted.error");
			else if (oops.getMessage().equals("You can't change the status of this enrolment because the deadline elapsed"))
				result = this.createEditModelAndView(enrolments, "enrolment.error.deadlineElapsed");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(enrolments, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(enrolments, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(enrolments, "commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Enrolment enrolment) {
		ModelAndView result;
		result = this.createEditModelAndView(enrolment, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Enrolment enrolment, final String message) {
		ModelAndView result;

		if (enrolment == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (enrolment.getId() == 0) {
			result = new ModelAndView("enrolment/create");

			final Collection<Activity> activities = this.activityService.findActivitiesAvailables();
			result.addObject("activities", activities);
		} else
			result = new ModelAndView("enrolment/edit");

		result.addObject("enrolment", enrolment);
		result.addObject("actionURL", "enrolment/visitor/edit.do");
		result.addObject("message", message);

		return result;
	}
	protected ModelAndView createEditModelAndView(final Collection<Enrolment> enrolments) {
		ModelAndView result;
		result = this.createEditModelAndView(enrolments, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Enrolment> enrolments, final String message) {
		ModelAndView result;

		if (enrolments == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("enrolment/list");

		final Date currentMoment = new Date(System.currentTimeMillis());

		result.addObject("enrolments", enrolments);
		result.addObject("currentMoment", currentMoment);
		result.addObject("requestURI", "enrolment/visitor/list.do");
		result.addObject("message", message);

		return result;
	}
}
