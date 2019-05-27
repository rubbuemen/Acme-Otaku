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
import services.ReportService;
import domain.Report;

@Controller
@RequestMapping("/report")
public class ReportController extends AbstractController {

	@Autowired
	ReportService	reportService;

	@Autowired
	ActorService	actorService;


	@RequestMapping(value = "/listGeneric", method = RequestMethod.GET)
	public ModelAndView listReports() {
		ModelAndView result;
		Collection<Report> reports;

		reports = this.reportService.findAll();

		result = new ModelAndView("report/listGeneric");

		result.addObject("reports", reports);
		result.addObject("requestURI", "report/listGeneric.do");

		return result;
	}

}
