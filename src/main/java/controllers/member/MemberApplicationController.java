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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.ApplicationService;
import services.AssociationService;
import controllers.AbstractController;
import domain.Actor;
import domain.Application;
import domain.Association;
import domain.Member;

@Controller
@RequestMapping("/application/member")
public class MemberApplicationController extends AbstractController {

	@Autowired
	ApplicationService	applicationService;

	@Autowired
	AssociationService	associationService;

	@Autowired
	ActorService		actorService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Application> applications;

		applications = this.applicationService.findApplicationsByMemberLogged();

		result = new ModelAndView("application/list");

		final Actor actorLogged = this.actorService.findActorLogged();
		final Member memberLogged = (Member) actorLogged;
		result.addObject("memberLogged", memberLogged);

		result.addObject("applications", applications);
		result.addObject("requestURI", "application/member/list.do");

		return result;
	}

	@RequestMapping(value = "/listPresident", method = RequestMethod.GET)
	public ModelAndView listPresident() {
		ModelAndView result;
		Collection<Application> applications = null;

		try {
			applications = this.applicationService.findApplicationsByPresidentLogged();
			result = this.createEditModelAndView(applications);
			final Actor actorLogged = this.actorService.findActorLogged();
			final Member memberLogged = (Member) actorLogged;
			result.addObject("memberLogged", memberLogged);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association to see the applications"))
				result = this.createEditModelAndView(applications, "application.notPresident.error");
			else
				result = this.createEditModelAndView(applications, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Application application = null;

		try {
			application = this.applicationService.create();
			result = this.createEditModelAndView(application);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You already belong to an association"))
				result = this.createEditModelAndView(application, "association.alreadyBelongs.error");
			else
				result = this.createEditModelAndView(application, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Application application, final BindingResult binding) {
		ModelAndView result;

		try {
			application = this.applicationService.reconstruct(application, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(application);
			else {
				this.applicationService.save(application);
				result = new ModelAndView("redirect:/application/member/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You already belong to an association"))
				result = this.createEditModelAndView(application, "association.alreadyBelongs.error");
			else if (oops.getMessage().equals("You already have an application for this association pending"))
				result = this.createEditModelAndView(application, "application.pending.error");
			else if (oops.getMessage().equals("You cannot currently join this association"))
				result = this.createEditModelAndView(application, "application.cantJoin.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(application, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(application, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/accept", method = RequestMethod.GET)
	public ModelAndView acceptApplication(@RequestParam final int applicationId) {
		ModelAndView result;

		final Application application = this.applicationService.findOne(applicationId);
		Collection<Application> applications = null;

		try {
			applications = this.applicationService.findApplicationsByPresidentLogged();
			this.applicationService.acceptApplication(application);
			result = new ModelAndView("redirect:/application/member/listPresident.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association to see the applications"))
				result = this.createEditModelAndView(applications, "application.notPresident.error");
			else if (oops.getMessage().equals("You have to be the president of the association to accept an application"))
				result = this.createEditModelAndView(applications, "application.error.noPresidentAccept");
			else if (oops.getMessage().equals("These applications do not belong to your association"))
				result = this.createEditModelAndView(applications, "application.error.notBelongsAssociation");
			else if (oops.getMessage().equals("The status of this application is not 'pending'"))
				result = this.createEditModelAndView(applications, "application.error.notPending");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(applications, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(applications, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/decline", method = RequestMethod.GET)
	public ModelAndView declineApplication(@RequestParam final int applicationId) {
		ModelAndView result;

		final Application application = this.applicationService.findOne(applicationId);
		Collection<Application> applications = null;

		try {
			applications = this.applicationService.findApplicationsByPresidentLogged();
			this.applicationService.declineApplication(application);
			result = new ModelAndView("redirect:/application/member/listPresident.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association to see the applications"))
				result = this.createEditModelAndView(applications, "application.notPresident.error");
			else if (oops.getMessage().equals("You have to be the president of the association to decline an application"))
				result = this.createEditModelAndView(applications, "application.error.noPresidentDecline");
			else if (oops.getMessage().equals("These applications do not belong to your association"))
				result = this.createEditModelAndView(applications, "application.error.notBelongsAssociation");
			else if (oops.getMessage().equals("The status of this application is not 'pending'"))
				result = this.createEditModelAndView(applications, "application.error.notPending");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(applications, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(applications, "commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Application application) {
		ModelAndView result;
		result = this.createEditModelAndView(application, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Application application, final String message) {
		ModelAndView result;

		if (application == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (application.getId() == 0) {
			result = new ModelAndView("application/create");
			final Collection<Association> associations = this.associationService.findAssociationsToJoin();
			result.addObject("associations", associations);
		} else
			result = new ModelAndView("application/edit");

		result.addObject("application", application);
		result.addObject("actionURL", "application/member/edit.do");
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Application> applications) {
		ModelAndView result;
		result = this.createEditModelAndView(applications, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Application> applications, final String message) {
		ModelAndView result;

		if (applications == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("application/list");

		final Actor actorLogged = this.actorService.findActorLogged();
		final Member memberLogged = (Member) actorLogged;
		result.addObject("memberLogged", memberLogged);

		result.addObject("applications", applications);
		result.addObject("requestURI", "application/member/listPresident.do");
		result.addObject("message", message);

		return result;
	}
}
