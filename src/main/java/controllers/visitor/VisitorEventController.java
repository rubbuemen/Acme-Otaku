
package controllers.visitor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.EventService;
import controllers.AbstractController;
import domain.Event;

@Controller
@RequestMapping("/event/visitor")
public class VisitorEventController extends AbstractController {

	@Autowired
	EventService	eventService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Event> events;

		events = this.eventService.findEventsFinalModeNotFinished();

		result = new ModelAndView("event/list");

		result.addObject("events", events);
		result.addObject("requestURI", "event/visitor/list.do");

		return result;
	}

}
