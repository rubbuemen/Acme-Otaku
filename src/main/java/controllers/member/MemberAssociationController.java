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
import services.AssociationService;
import services.MemberService;
import controllers.AbstractController;
import domain.Actor;
import domain.Association;
import domain.Member;

@Controller
@RequestMapping("/association/member")
public class MemberAssociationController extends AbstractController {

	@Autowired
	AssociationService	associationService;

	@Autowired
	ActorService		actorService;

	@Autowired
	MemberService		memberService;


	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView show() {
		ModelAndView result;
		Association association;

		association = this.associationService.findAssociationByMemberLogged();

		final Actor actorLogged = this.actorService.findActorLogged();
		final Member memberLogged = (Member) actorLogged;

		result = new ModelAndView("association/show");

		result.addObject("association", association);
		result.addObject("memberLogged", memberLogged);
		result.addObject("requestURI", "association/member/show.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Association association = null;

		try {
			association = this.associationService.create();
			result = this.createEditModelAndView(association);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You already belong to an association"))
				result = this.createEditModelAndView(association, "association.alreadyBelongs.error");
			else
				result = this.createEditModelAndView(association, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int associationId) {
		ModelAndView result;
		Association association = null;

		try {
			association = this.associationService.findAssociationMemberLogged(associationId);
			result = this.createEditModelAndView(association);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association in order to edit it"))
				result = this.createEditModelAndView(association, "association.notPresident.error");
			else if (oops.getMessage().equals("You don't belong to this association"))
				result = this.createEditModelAndView(association, "association.notBelongs.error");
			else
				result = this.createEditModelAndView(association, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Association association, final BindingResult binding) {
		ModelAndView result;

		try {
			association = this.associationService.reconstruct(association, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(association);
			else {
				this.associationService.save(association);
				result = new ModelAndView("redirect:/association/member/show.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association in order to edit it"))
				result = this.createEditModelAndView(association, "association.notPresident.error");
			else if (oops.getMessage().equals("You don't belong to this association"))
				result = this.createEditModelAndView(association, "association.notBelongs.error");
			else if (oops.getMessage().equals("You already belong to an association"))
				result = this.createEditModelAndView(association, "association.alreadyBelongs.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(association, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(association, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/allowToJoin", method = RequestMethod.GET)
	public ModelAndView allowMembers(@RequestParam final int associationId) {
		ModelAndView result;
		Association association = null;

		try {
			association = this.associationService.findAssociationMemberLogged(associationId);
			this.associationService.allowMembers(association);
			result = new ModelAndView("redirect:/association/member/show.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association in order to edit it"))
				result = this.createEditModelAndView(association, "association.notPresident.error");
			else if (oops.getMessage().equals("You don't belong to this association"))
				result = this.createEditModelAndView(association, "association.notBelongs.error");
			else if (oops.getMessage().equals("This association already allows members"))
				result = this.createEditModelAndView(association, "association.allowMembers.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(association, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(association, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/notAllowToJoin", method = RequestMethod.GET)
	public ModelAndView notAllowMembers(@RequestParam final int associationId) {
		ModelAndView result;
		Association association = null;

		try {
			association = this.associationService.findAssociationMemberLogged(associationId);
			this.associationService.notAllowMembers(association);
			result = new ModelAndView("redirect:/association/member/show.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association in order to edit it"))
				result = this.createEditModelAndView(association, "association.notPresident.error");
			else if (oops.getMessage().equals("You don't belong to this association"))
				result = this.createEditModelAndView(association, "association.notBelongs.error");
			else if (oops.getMessage().equals("This association no longer allows members"))
				result = this.createEditModelAndView(association, "association.notAllowMembers.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(association, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(association, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/leave", method = RequestMethod.GET)
	public ModelAndView leave() {
		ModelAndView result;

		final Actor actorLogged = this.actorService.findActorLogged();
		final Member memberLogged = (Member) actorLogged;
		final Collection<Member> membersAssociation = this.memberService.findMembersByAssociationMemberLogged();
		membersAssociation.remove(memberLogged);

		result = new ModelAndView("association/leave");
		result.addObject("members", membersAssociation);
		result.addObject("role", memberLogged.getRole());
		result.addObject("actionURL", "association/member/leave.do");

		return result;
	}

	@RequestMapping(value = "/leave", method = RequestMethod.POST, params = "leave")
	public ModelAndView leave(@RequestParam(required = false) final Integer memberId) {
		ModelAndView result;
		Member member = null;

		try {
			if (memberId != null)
				member = this.memberService.findOne(memberId);
			this.associationService.leave(memberId);
			result = new ModelAndView("redirect:/association/member/show.do");
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The selected member does not belong to your association"))
				result = this.createEditModelAndView(member, "association.memberNotBelongs.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(member, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(member, "commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Association association) {
		ModelAndView result;
		result = this.createEditModelAndView(association, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Association association, final String message) {
		ModelAndView result;

		if (association == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (association.getId() == 0)
			result = new ModelAndView("association/create");
		else
			result = new ModelAndView("association/edit");

		result.addObject("association", association);
		result.addObject("actionURL", "association/member/edit.do");
		result.addObject("message", message);

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Member member) {
		ModelAndView result;
		result = this.createEditModelAndView(member, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Member member, final String message) {
		ModelAndView result;

		if (member == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("association/leave");

		final Actor actorLogged = this.actorService.findActorLogged();
		final Member memberLogged = (Member) actorLogged;
		final Collection<Member> membersAssociation = this.memberService.findMembersByAssociationMemberLogged();
		membersAssociation.remove(memberLogged);

		result.addObject("members", membersAssociation);
		result.addObject("member", member);
		result.addObject("role", memberLogged.getRole());
		result.addObject("actionURL", "association/member/leave.do");
		result.addObject("message", message);

		return result;
	}

}
