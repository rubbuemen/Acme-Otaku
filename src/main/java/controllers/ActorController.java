/*
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AdministratorService;
import services.MemberService;
import services.SellerService;
import services.SponsorService;
import services.UserAccountService;
import services.VisitorService;
import domain.Actor;
import domain.Member;
import domain.Seller;
import domain.Sponsor;
import domain.Visitor;
import forms.MemberForm;
import forms.SellerForm;
import forms.SponsorForm;
import forms.VisitorForm;

@Controller
@RequestMapping("/actor")
public class ActorController extends AbstractController {

	@Autowired
	ActorService			actorService;

	@Autowired
	VisitorService			visitorService;

	@Autowired
	MemberService			memberService;

	@Autowired
	SellerService			sellerService;

	@Autowired
	SponsorService			sponsorService;

	@Autowired
	AdministratorService	administratorService;

	@Autowired
	UserAccountService		userAccountService;


	@RequestMapping(value = "/register-visitor", method = RequestMethod.GET)
	public ModelAndView registerVisitor() {
		ModelAndView result;
		Visitor actor;

		actor = this.visitorService.create();

		final VisitorForm actorForm = new VisitorForm(actor);

		result = new ModelAndView("actor/register");

		result.addObject("actionURL", "actor/register-visitor.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/register-member", method = RequestMethod.GET)
	public ModelAndView registerMember() {
		ModelAndView result;
		Member actor;

		actor = this.memberService.create();

		final MemberForm actorForm = new MemberForm(actor);

		result = new ModelAndView("actor/register");

		result.addObject("actionURL", "actor/register-member.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/register-seller", method = RequestMethod.GET)
	public ModelAndView registerSeller() {
		ModelAndView result;
		Seller actor;

		actor = this.sellerService.create();

		final SellerForm actorForm = new SellerForm(actor);

		result = new ModelAndView("actor/register");

		result.addObject("actionURL", "actor/register-seller.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/register-sponsor", method = RequestMethod.GET)
	public ModelAndView registerSponsor() {
		ModelAndView result;
		Sponsor actor;

		actor = this.sponsorService.create();

		final SponsorForm actorForm = new SponsorForm(actor);

		result = new ModelAndView("actor/register");

		result.addObject("actionURL", "actor/register-sponsor.do");
		result.addObject("actorForm", actorForm);

		return result;
	}

	@RequestMapping(value = "/register-visitor", method = RequestMethod.POST, params = "save")
	public ModelAndView registerVisitor(@ModelAttribute("actorForm") VisitorForm actorForm, final BindingResult binding) {
		ModelAndView result;

		actorForm = this.visitorService.reconstruct(actorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm.getActor());
		else
			try {
				Assert.isTrue(actorForm.getActor().getUserAccount().getPassword().equals(actorForm.getPasswordCheck()), "Password does not match");
				Assert.isTrue(actorForm.getTermsConditions(), "The terms and conditions must be accepted");
				this.visitorService.save(actorForm.getActor());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("Password does not match"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.password.match");
				else if (oops.getMessage().equals("The terms and conditions must be accepted"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.error.duplicate.user");
				else if (oops.getMessage().equals("This entity does not exist"))
					result = this.createEditModelAndView(null, "hacking.notExist.error");
				else
					result = this.createEditModelAndView(actorForm.getActor(), "commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/register-member", method = RequestMethod.POST, params = "save")
	public ModelAndView registerMember(@ModelAttribute("actorForm") MemberForm actorForm, final BindingResult binding) {
		ModelAndView result;

		actorForm = this.memberService.reconstruct(actorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm.getActor());
		else
			try {
				Assert.isTrue(actorForm.getActor().getUserAccount().getPassword().equals(actorForm.getPasswordCheck()), "Password does not match");
				Assert.isTrue(actorForm.getTermsConditions(), "The terms and conditions must be accepted");
				this.memberService.save(actorForm.getActor());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("Password does not match"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.password.match");
				else if (oops.getMessage().equals("The terms and conditions must be accepted"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.error.duplicate.user");
				else if (oops.getMessage().equals("This entity does not exist"))
					result = this.createEditModelAndView(null, "hacking.notExist.error");
				else
					result = this.createEditModelAndView(actorForm.getActor(), "commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/register-seller", method = RequestMethod.POST, params = "save")
	public ModelAndView registerSeller(@ModelAttribute("actorForm") SellerForm actorForm, final BindingResult binding) {
		ModelAndView result;

		actorForm = this.sellerService.reconstruct(actorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm.getActor());
		else
			try {
				Assert.isTrue(actorForm.getActor().getUserAccount().getPassword().equals(actorForm.getPasswordCheck()), "Password does not match");
				Assert.isTrue(actorForm.getTermsConditions(), "The terms and conditions must be accepted");
				this.sellerService.save(actorForm.getActor());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("Password does not match"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.password.match");
				else if (oops.getMessage().equals("The terms and conditions must be accepted"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.error.duplicate.user");
				else if (oops.getMessage().equals("This entity does not exist"))
					result = this.createEditModelAndView(null, "hacking.notExist.error");
				else
					result = this.createEditModelAndView(actorForm.getActor(), "commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/register-sponsor", method = RequestMethod.POST, params = "save")
	public ModelAndView registerSponsor(@ModelAttribute("actorForm") SponsorForm actorForm, final BindingResult binding) {
		ModelAndView result;

		actorForm = this.sponsorService.reconstruct(actorForm, binding);

		if (binding.hasErrors())
			result = this.createEditModelAndView(actorForm.getActor());
		else
			try {
				Assert.isTrue(actorForm.getActor().getUserAccount().getPassword().equals(actorForm.getPasswordCheck()), "Password does not match");
				Assert.isTrue(actorForm.getTermsConditions(), "The terms and conditions must be accepted");
				this.sponsorService.save(actorForm.getActor());
				result = new ModelAndView("redirect:/welcome/index.do");
			} catch (final Throwable oops) {
				if (oops.getMessage().equals("Password does not match"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.password.match");
				else if (oops.getMessage().equals("The terms and conditions must be accepted"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.conditions.accept");
				else if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
					result = this.createEditModelAndView(actorForm.getActor(), "actor.error.duplicate.user");
				else if (oops.getMessage().equals("This entity does not exist"))
					result = this.createEditModelAndView(null, "hacking.notExist.error");
				else
					result = this.createEditModelAndView(actorForm.getActor(), "commit.error");
			}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete() {
		ModelAndView result;

		final Actor actor = this.actorService.findActorLogged();

		try {
			if (actor instanceof Visitor)
				this.visitorService.delete((Visitor) actor);
			else if (actor instanceof Member)
				this.memberService.delete((Member) actor);
			else if (actor instanceof Seller)
				this.sellerService.delete((Seller) actor);
			else if (actor instanceof Sponsor)
				this.sponsorService.delete((Sponsor) actor);
			result = new ModelAndView("redirect:/j_spring_security_logout");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(actor, "commit.error");

		}

		return result;
	}

	//	@RequestMapping(value = "/export", method = RequestMethod.GET)
	//	@ResponseBody
	//	public ModelAndView exportData(final HttpServletResponse response) {
	//		ModelAndView result;
	//		try {
	//			final StringBuilder sb = this.actorService.exportData();
	//			response.setContentType("text/csv");
	//			response.setHeader("Content-Disposition", "attachment;filename=data.csv");
	//			final ServletOutputStream outStream = response.getOutputStream();
	//			outStream.println(sb.toString());
	//			outStream.flush();
	//			outStream.close();
	//			result = new ModelAndView("redirect:/welcome/index.do");
	//		} catch (final Throwable oops) {
	//			result = this.createEditModelAndView(null, "commit.error");
	//		}
	//
	//		return result;
	//	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Actor actor) {
		ModelAndView result;
		result = this.createEditModelAndView(actor, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Actor actor, final String message) {
		ModelAndView result;
		if (actor == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("actor/register");

		result.addObject("actor", actor);
		result.addObject("message", message);

		return result;
	}

}
