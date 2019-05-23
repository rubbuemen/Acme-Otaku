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

import services.ActorService;
import services.MemberService;
import controllers.AbstractController;
import domain.Member;

@Controller
@RequestMapping("/member/member")
public class MemberMemberController extends AbstractController {

	@Autowired
	MemberService	memberService;

	@Autowired
	ActorService	actorService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Member> members = null;

		try {
			members = this.memberService.findMembersByAssociationPresidentLogged();
			result = this.createEditModelAndView(members);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association to see the members"))
				result = this.createEditModelAndView(members, "member.notPresident.error");
			else
				result = this.createEditModelAndView(members, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int memberId) {
		ModelAndView result;
		Member member = null;

		try {
			member = this.memberService.findMemberByPresidentLogged(memberId);
			result = this.createEditModelAndView(member);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association to see the members"))
				result = this.createEditModelAndView(member, "member.notPresident.error");
			else if (oops.getMessage().equals("This member do not belong to your association"))
				result = this.createEditModelAndView(member, "member.notBelongs.error");
			else
				result = this.createEditModelAndView(member, "commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Member member, final BindingResult binding) {
		ModelAndView result;

		try {
			member = this.memberService.reconstruct(member, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(member);
			else {
				this.memberService.changeRole(member);
				result = new ModelAndView("redirect:/member/member/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have to be the president of the association to change a member's role"))
				result = this.createEditModelAndView(member, "member.notPresidentRole.error");
			else if (oops.getMessage().equals("This member do not belong to your association"))
				result = this.createEditModelAndView(member, "member.notBelongs.error");
			else if (oops.getMessage().equals("There can only be one president in the association"))
				result = this.createEditModelAndView(member, "member.onePresident.error");
			else if (oops.getMessage().equals("There can only be one vice president in the association"))
				result = this.createEditModelAndView(member, "member.oneVicePresident.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(member, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(member, "commit.error");
		}

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
		else {
			result = new ModelAndView("member/edit");
			final Collection<String> roles = new HashSet<>();
			Collections.addAll(roles, "PRESIDENT", "VICEPRESIDENT", "SECRETARY", "TREASURE", "VOCAL", "MEMBER");
			result.addObject("roles", roles);
		}

		result.addObject("member", member);
		result.addObject("actionURL", "member/member/edit.do");
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Member> members) {
		ModelAndView result;
		result = this.createEditModelAndView(members, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Member> members, final String message) {
		ModelAndView result;

		if (members == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("member/list");

		result.addObject("members", members);
		result.addObject("requestURI", "member/member/list.do");
		result.addObject("message", message);

		return result;
	}

}
