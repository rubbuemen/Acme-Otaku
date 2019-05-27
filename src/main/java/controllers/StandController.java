/*
 * AdministratorController.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.StandService;
import domain.Stand;

@Controller
@RequestMapping("/stand")
public class StandController extends AbstractController {

	@Autowired
	StandService	standService;

	@Autowired
	ActorService	actorService;


	@RequestMapping(value = "/listGeneric", method = RequestMethod.GET)
	public ModelAndView listStands() {
		ModelAndView result;
		Collection<Stand> stands;

		stands = this.standService.findAll();

		result = new ModelAndView("stand/listGeneric");

		result.addObject("stands", stands);
		result.addObject("requestURI", "stand/listGeneric.do");

		return result;
	}

}
