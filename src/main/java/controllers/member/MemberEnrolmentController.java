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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AssociationService;
import services.EnrolmentService;
import controllers.AbstractController;
import domain.Actor;
import domain.Enrolment;
import domain.Member;

@Controller
@RequestMapping("/enrolment/member")
public class MemberEnrolmentController extends AbstractController {

	@Autowired
	EnrolmentService	enrolmentService;

	@Autowired
	AssociationService	associationService;

	@Autowired
	ActorService		actorService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Enrolment> enrolments = null;

		try {
			enrolments = this.enrolmentService.findEnrolmentsByAssociationMemberLogged();
			result = this.createEditModelAndView(enrolments);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You need to belong to an association to manage activity enrolments"))
				result = this.createEditModelAndView(enrolments, "enrolment.error.notBelongs");
			else
				result = this.createEditModelAndView(enrolments, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView acceptEnrolment(@RequestParam final int enrolmentId) {
		ModelAndView result;

		final Enrolment enrolment = this.enrolmentService.findOne(enrolmentId);
		Collection<Enrolment> enrolments = null;

		try {
			enrolments = this.enrolmentService.findEnrolmentsByAssociationMemberLogged();
			this.enrolmentService.acceptEnrolment(enrolment);
			result = new ModelAndView("redirect:/enrolment/member/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This enrolment do not belong to your association"))
				result = this.createEditModelAndView(enrolments, "enrolment.error.notOwner");
			else if (oops.getMessage().equals("The status of this enrolment is not 'pending'"))
				result = this.createEditModelAndView(enrolments, "enrolment.error.notPending");
			else if (oops.getMessage().equals("You can't change the status of this enrolment because the deadline elapsed"))
				result = this.createEditModelAndView(enrolments, "enrolment.error.deadlineElapsed");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(enrolments, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(enrolments, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/decline", method = RequestMethod.GET)
	public ModelAndView declineEnrolment(@RequestParam final int enrolmentId) {
		ModelAndView result;

		final Enrolment enrolment = this.enrolmentService.findOne(enrolmentId);
		Collection<Enrolment> enrolments = null;

		try {
			enrolments = this.enrolmentService.findEnrolmentsByAssociationMemberLogged();
			this.enrolmentService.declineEnrolment(enrolment);
			result = new ModelAndView("redirect:/enrolment/member/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This enrolment do not belong to your association"))
				result = this.createEditModelAndView(enrolments, "enrolment.error.notOwner");
			else if (oops.getMessage().equals("The status of this enrolment is not 'pending'"))
				result = this.createEditModelAndView(enrolments, "enrolment.error.notPending");
			else if (oops.getMessage().equals("You can't change the status of this enrolment because the deadline elapsed"))
				result = this.createEditModelAndView(enrolments, "enrolment.error.deadlineElapsed");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(enrolments, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(enrolments, "commit.error");
		}

		return result;
	}

	// Ancillary methods
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
		final Actor actorLogged = this.actorService.findActorLogged();
		final Member memberLogged = (Member) actorLogged;
		result.addObject("memberLogged", memberLogged);

		result.addObject("enrolments", enrolments);
		result.addObject("currentMoment", currentMoment);
		result.addObject("requestURI", "enrolment/member/list.do");
		result.addObject("message", message);

		return result;
	}
}
