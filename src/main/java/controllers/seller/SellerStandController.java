
package controllers.seller;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.EventService;
import services.StandService;
import controllers.AbstractController;
import domain.Event;
import domain.Stand;

@Controller
@RequestMapping("/stand/seller")
public class SellerStandController extends AbstractController {

	@Autowired
	StandService	standService;

	@Autowired
	EventService	eventService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Stand> stands;

		stands = this.standService.findStandsBySellerLogged();

		result = new ModelAndView("stand/list");

		result.addObject("stands", stands);
		result.addObject("requestURI", "stand/seller/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Stand stand;

		stand = this.standService.create();

		result = this.createEditModelAndView(stand);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int standId) {
		ModelAndView result;
		Stand stand = null;

		try {
			stand = this.standService.findStandSellerLogged(standId);

			Assert.isTrue(stand.getEvents().isEmpty(), "You can not edit the stand because it has some associated event");

			result = this.createEditModelAndView(stand);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can not edit the stand because it has some associated event"))
				result = this.createEditModelAndView(stand, "stand.error.save.associatedEvent");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(stand, "hacking.logged.error");
			else
				result = this.createEditModelAndView(stand, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Stand stand, final BindingResult binding) {
		ModelAndView result;

		try {
			stand = this.standService.reconstruct(stand, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(stand);
			else {
				this.standService.save(stand);
				result = new ModelAndView("redirect:/stand/seller/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The maximum limit of commercial stands is 5 in one of the selected events"))
				result = this.createEditModelAndView(stand, "stand.error.commercialStandLimit");
			else if (oops.getMessage().equals("The maximum limit of artisan stands is 4 in one of the selected events"))
				result = this.createEditModelAndView(stand, "stand.error.artisanStandLimit");
			else if (oops.getMessage().equals("The maximum limit of food stands is 4 in one of the selected events"))
				result = this.createEditModelAndView(stand, "stand.error.foodStandLimit");
			else if (oops.getMessage().equals("This event is not available to associate it with a stand"))
				result = this.createEditModelAndView(stand, "stand.error.save.stand.hacking");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(stand, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(stand, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int standId) {
		ModelAndView result;

		final Stand stand = this.standService.findStandSellerLogged(standId);

		try {
			this.standService.delete(stand);
			result = new ModelAndView("redirect:/stand/seller/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You can not delete the stand because it has some associated event"))
				result = this.createEditModelAndView(stand, "stand.error.delete.associatedEvent");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(stand, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(stand, "commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Stand stand) {
		ModelAndView result;
		result = this.createEditModelAndView(stand, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Stand stand, final String message) {
		ModelAndView result;

		if (stand == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			if (stand.getId() == 0)
				result = new ModelAndView("stand/create");
			else
				result = new ModelAndView("stand/edit");
			final Collection<Event> events = this.eventService.findEventsFinalModeNotFinished();
			final Collection<String> types = new HashSet<>();
			Collections.addAll(types, "COMMERCIAL", "ARTISAN", "FOOD");
			result.addObject("events", events);
			result.addObject("types", types);
		}

		result.addObject("stand", stand);
		result.addObject("actionURL", "stand/seller/edit.do");
		result.addObject("message", message);

		return result;
	}

}
