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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;
import services.ActorService;
import services.CategoryService;
import controllers.AbstractController;
import domain.Activity;
import domain.Category;

@Controller
@RequestMapping("/activity/member")
public class MemberActivityController extends AbstractController {

	@Autowired
	ActivityService	activityService;

	@Autowired
	ActorService	actorService;

	@Autowired
	CategoryService	categoryService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Activity> activities = null;

		try {
			activities = this.activityService.findActivitiesByMemberLogged();
			result = this.createEditModelAndView(activities);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You need to belong to an association to manage activities"))
				result = this.createEditModelAndView(activities, "activity.notBelongs.error");
			else
				result = this.createEditModelAndView(activities, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Activity activity = null;

		try {
			activity = this.activityService.create();
			result = this.createEditModelAndView(activity);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You need to belong to an association to manage activities"))
				result = this.createEditModelAndView(activity, "activity.notBelongs.error");
			else
				result = this.createEditModelAndView(activity, "commit.error");
		}

		return result;
	}
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int activityId) {
		ModelAndView result;
		Activity activity = null;

		try {
			activity = this.activityService.findActivityMemberLogged(activityId);
			result = this.createEditModelAndView(activity, null);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(activity, "hacking.logged.error");
			else
				result = this.createEditModelAndView(activity, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Activity activity, final BindingResult binding) {
		ModelAndView result;

		try {
			activity = this.activityService.reconstruct(activity, binding);

			if (binding.hasErrors())
				result = this.createEditModelAndView(activity);
			else {
				this.activityService.save(activity);
				result = new ModelAndView("redirect:/activity/member/list.do");
			}

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You need to belong to an association to manage activities"))
				result = this.createEditModelAndView(activity, "activity.notBelongs.error");
			else if (oops.getMessage().equals("You can only save activities that are not in final mode"))
				result = this.createEditModelAndView(activity, "activity.error.save.finalMode");
			else if (oops.getMessage().equals("The deadline must be future"))
				result = this.createEditModelAndView(activity, "activity.error.deadlineNotFuture");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(activity, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(activity, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(activity, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int activityId) {
		ModelAndView result;

		Activity activity = null;

		try {
			activity = this.activityService.findActivityMemberLogged(activityId);
			this.activityService.delete(activity);
			result = new ModelAndView("redirect:/activity/member/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can only delete activities that are not in final mode"))
				result = this.createEditModelAndView(activity, "activity.error.delete.finalMode");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(activity, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(activity, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(activity, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/change", method = RequestMethod.GET)
	public ModelAndView changeFinalMode(@RequestParam final int activityId) {
		ModelAndView result;

		Activity activity = null;

		try {
			activity = this.activityService.findActivityMemberLogged(activityId);
			this.activityService.changeFinalMode(activity);
			result = new ModelAndView("redirect:/activity/member/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This activity is already in final mode"))
				result = this.createEditModelAndView(activity, "activity.error.change.finalMode");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(activity, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(activity, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(activity, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/finish", method = RequestMethod.GET)
	public ModelAndView finish(@RequestParam final int activityId) {
		ModelAndView result;

		Activity activity = null;

		try {
			activity = this.activityService.findActivityMemberLogged(activityId);
			this.activityService.finish(activity);
			result = new ModelAndView("redirect:/activity/member/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This activity is already finished"))
				result = this.createEditModelAndView(activity, "activity.error.alreadyFinished");
			else if (oops.getMessage().equals("Cannot finish an activity that is not in final mode"))
				result = this.createEditModelAndView(activity, "activity.error.notFinalMode");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(activity, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(activity, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(activity, "commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Activity activity) {
		ModelAndView result;
		result = this.createEditModelAndView(activity, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Activity activity, final String message) {
		ModelAndView result;

		if (activity == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (activity.getId() == 0)
			result = new ModelAndView("activity/create");
		else
			result = new ModelAndView("activity/edit");

		final String language = LocaleContextHolder.getLocale().getLanguage();
		final Collection<Category> categories = this.categoryService.findAll();
		result.addObject("language", language);
		result.addObject("categories", categories);

		result.addObject("activity", activity);
		result.addObject("actionURL", "activity/member/edit.do");
		result.addObject("message", message);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Activity> activities) {
		ModelAndView result;
		result = this.createEditModelAndView(activities, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Collection<Activity> activities, final String message) {
		ModelAndView result;

		if (activities == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else
			result = new ModelAndView("activity/list");

		final String language = LocaleContextHolder.getLocale().getLanguage();
		result.addObject("language", language);
		result.addObject("activities", activities);
		result.addObject("requestURI", "activity/member/list.do");
		result.addObject("message", message);

		return result;
	}
}
