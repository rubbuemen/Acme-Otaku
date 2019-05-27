/*
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.seller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.DayService;
import services.EventService;
import services.SystemConfigurationService;
import controllers.AbstractController;
import domain.Day;
import domain.Event;

@Controller
@RequestMapping("/day/seller")
public class SellerDayController extends AbstractController {

	@Autowired
	DayService					dayService;

	@Autowired
	EventService				eventService;

	@Autowired
	SystemConfigurationService	systemConfigurationService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int eventId) {
		ModelAndView result;
		Collection<Day> days;
		final Event event = this.eventService.findOne(eventId);

		final Double vatPercentage = this.systemConfigurationService.getConfiguration().getVATPercentage();

		days = event.getDays();
		result = new ModelAndView("day/list");
		result.addObject("days", days);
		result.addObject("vatPercentage", vatPercentage);
		result.addObject("requestURI", "day/seller/list.do");
		result.addObject("event", event);

		return result;
	}

}
