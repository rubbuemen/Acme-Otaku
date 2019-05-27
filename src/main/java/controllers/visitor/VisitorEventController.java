
package controllers.visitor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.EventService;
import services.SponsorshipService;
import controllers.AbstractController;
import domain.Event;
import domain.Sponsorship;

@Controller
@RequestMapping("/event/visitor")
public class VisitorEventController extends AbstractController {

	@Autowired
	EventService		eventService;

	@Autowired
	SponsorshipService	sponsorshipService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Event> events;

		events = this.eventService.findEventsFinalModeNotFinished();

		result = new ModelAndView("event/list");

		result.addObject("events", events);
		result.addObject("requestURI", "event/visitor/list.do");

		if (!events.isEmpty()) {
			final Map<Event, Sponsorship> randomSponsorship = new HashMap<>();
			for (final Event e : events) {
				final Sponsorship sponsorship = this.sponsorshipService.findRandomSponsorship(e.getId());
				if (sponsorship != null)
					randomSponsorship.put(e, sponsorship);
			}
			result.addObject("randomSponsorship", randomSponsorship);
		}

		return result;
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST, params = "search")
	public ModelAndView searchEvent(final String singleKeyWord) {
		ModelAndView result;
		Collection<Event> events;
		events = this.eventService.findEventsFinalModeNotFinishedBySingleKeyWord(singleKeyWord);

		result = new ModelAndView("event/list");

		result.addObject("events", events);
		result.addObject("requestURI", "event/visitor/list.do");

		if (!events.isEmpty()) {
			final Map<Event, Sponsorship> randomSponsorship = new HashMap<>();
			for (final Event e : events) {
				final Sponsorship sponsorship = this.sponsorshipService.findRandomSponsorship(e.getId());
				if (sponsorship != null)
					randomSponsorship.put(e, sponsorship);
			}
			result.addObject("randomSponsorship", randomSponsorship);
		}

		return result;
	}

}
