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

import services.HeadquarterService;
import controllers.AbstractController;
import domain.Headquarter;

@Controller
@RequestMapping("/headquarter/member")
public class MemberHeadquarterController extends AbstractController {

	@Autowired
	HeadquarterService	headquarterService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Headquarter> headquarters = null;

		try {
			headquarters = this.headquarterService.findHeadquartersByMemberLogged();
			result = this.createEditModelAndView(headquarters);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association for manage headquarters"))
				result = this.createEditModelAndView(headquarters, "headquarter.notPresident.error");
			else
				result = this.createEditModelAndView(headquarters, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Headquarter headquarter = null;

		try {
			headquarter = this.headquarterService.create();
			result = this.createEditModelAndView(headquarter);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association for manage headquarters"))
				result = this.createEditModelAndView(headquarter, "headquarter.notPresident.error");
			else
				result = this.createEditModelAndView(headquarter, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int headquarterId) {
		ModelAndView result;
		Headquarter headquarter = null;

		try {
			headquarter = this.headquarterService.findHeadquarterMemberLogged(headquarterId);
			result = this.createEditModelAndView(headquarter, null);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(headquarter, "hacking.logged.error");
			else
				result = this.createEditModelAndView(headquarter, "commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Headquarter headquarter, final BindingResult binding) {
		ModelAndView result;

		try {
			headquarter = this.headquarterService.reconstruct(headquarter, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(headquarter);
			else {
				this.headquarterService.save(headquarter);
				result = new ModelAndView("redirect:/headquarter/member/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association for manage headquarters"))
				result = this.createEditModelAndView(headquarter, "headquarter.notPresident.error");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(headquarter, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(headquarter, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(headquarter, "commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Headquarter headquarter) {
		ModelAndView result;
		result = this.createEditModelAndView(headquarter, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Headquarter headquarter, final String message) {
		ModelAndView result;

		if (headquarter == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (headquarter.getId() == 0)
			result = new ModelAndView("headquarter/create");
		else
			result = new ModelAndView("headquarter/edit");

		result.addObject("headquarter", headquarter);
		result.addObject("actionURL", "headquarter/member/edit.do");
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Headquarter> headquarters) {
		ModelAndView result;
		result = this.createEditModelAndView(headquarters, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Headquarter> headquarters, final String message) {
		ModelAndView result;

		if (headquarters == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("headquarter/list");

		result.addObject("headquarters", headquarters);
		result.addObject("requestURI", "headquarter/member/list.do");
		result.addObject("message", message);

		return result;
	}
}
