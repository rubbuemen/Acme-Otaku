
package controllers.visitor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;
import services.EventService;
import controllers.AbstractController;
import domain.Activity;
import domain.Event;

@Controller
@RequestMapping("/activity/visitor")
public class VisitorActivityController extends AbstractController {

	@Autowired
	ActivityService	activityService;

	@Autowired
	EventService	eventService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int eventId) {
		ModelAndView result;
		Collection<Activity> activities;
		final Event event = this.eventService.findOne(eventId);

		activities = this.activityService.findActivitiesFinalModeNotFinishedByEventId(eventId);
		final String language = LocaleContextHolder.getLocale().getLanguage();

		result = new ModelAndView("activity/list");
		result.addObject("activities", activities);
		result.addObject("requestURI", "activity/visitor/list.do");
		result.addObject("event", event);
		result.addObject("language", language);

		return result;
	}

}
