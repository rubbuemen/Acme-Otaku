
package controllers.visitor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ReportService;
import services.StandService;
import controllers.AbstractController;
import domain.Report;
import domain.Stand;

@Controller
@RequestMapping("/report/visitor")
public class VisitorReportController extends AbstractController {

	@Autowired
	ReportService	reportService;

	@Autowired
	StandService	standService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Report> reports;

		reports = this.reportService.findReportsByVisitorLogged();

		result = new ModelAndView("report/list");

		result.addObject("reports", reports);
		result.addObject("requestURI", "report/visitor/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Report report;

		report = this.reportService.create();

		result = this.createEditModelAndView(report);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int reportId) {
		ModelAndView result;
		Report report = null;

		try {
			report = this.reportService.findReportVisitorLogged(reportId);
			result = this.createEditModelAndView(report);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(report, "hacking.logged.error");
			else
				result = this.createEditModelAndView(report, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Report report, final BindingResult binding) {
		ModelAndView result;

		try {
			report = this.reportService.reconstruct(report, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(report);
			else {
				this.reportService.save(report);
				result = new ModelAndView("redirect:/report/visitor/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(report, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(report, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int reportId) {
		ModelAndView result;
		Report report = null;

		try {
			report = this.reportService.findReportVisitorLogged(reportId);
			this.reportService.delete(report);
			result = new ModelAndView("redirect:/report/visitor/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(report, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(report, "commit.error");
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Report report) {
		ModelAndView result;
		result = this.createEditModelAndView(report, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Report report, final String message) {
		ModelAndView result;

		if (report == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else {
			if (report.getId() == 0)
				result = new ModelAndView("report/create");
			else
				result = new ModelAndView("report/edit");
			final Collection<Stand> stands = this.standService.findAll();
			result.addObject("stands", stands);
		}

		result.addObject("report", report);
		result.addObject("actionURL", "report/visitor/edit.do");
		result.addObject("message", message);

		return result;
	}

}
