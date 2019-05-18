/*
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.administrator;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import controllers.AbstractController;
import domain.Company;
import domain.Position;
import domain.Provider;
import domain.Rookie;

@Controller
@RequestMapping("/dashboard/administrator")
public class AdministratorDashboardController extends AbstractController {

	@Autowired
	AdministratorService	administratorService;


	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView dashboard() {

		final ModelAndView result;

		result = new ModelAndView("dashboard/show");

		//Query C1
		final String[] queryC1 = this.administratorService.dashboardQueryC1().split(",");
		final String avgQueryC1 = queryC1[0];
		final String minQueryC1 = queryC1[1];
		final String maxQueryC1 = queryC1[2];
		final String stddevQueryC1 = queryC1[3];
		result.addObject("avgQueryC1", avgQueryC1);
		result.addObject("minQueryC1", minQueryC1);
		result.addObject("maxQueryC1", maxQueryC1);
		result.addObject("stddevQueryC1", stddevQueryC1);

		//Query C2
		final String[] queryC2 = this.administratorService.dashboardQueryC2().split(",");
		final String avgQueryC2 = queryC2[0];
		final String minQueryC2 = queryC2[1];
		final String maxQueryC2 = queryC2[2];
		final String stddevQueryC2 = queryC2[3];
		result.addObject("avgQueryC2", avgQueryC2);
		result.addObject("minQueryC2", minQueryC2);
		result.addObject("maxQueryC2", maxQueryC2);
		result.addObject("stddevQueryC2", stddevQueryC2);

		//Query C3
		final Collection<Company> queryC3 = this.administratorService.dashboardQueryC3();
		result.addObject("queryC3", queryC3);

		//Query C4
		final Collection<Rookie> queryC4 = this.administratorService.dashboardQueryC4();
		result.addObject("queryC4", queryC4);

		//Query C5
		final String[] queryC5 = this.administratorService.dashboardQueryC5().split(",");
		final String avgQueryC5 = queryC5[0];
		final String minQueryC5 = queryC5[1];
		final String maxQueryC5 = queryC5[2];
		final String stddevQueryC5 = queryC5[3];
		result.addObject("avgQueryC5", avgQueryC5);
		result.addObject("minQueryC5", minQueryC5);
		result.addObject("maxQueryC5", maxQueryC5);
		result.addObject("stddevQueryC5", stddevQueryC5);

		//Query C6
		final Position queryC6_1 = this.administratorService.dashboardQueryC6_1();
		result.addObject("queryC6_1", queryC6_1);
		final Position queryC6_2 = this.administratorService.dashboardQueryC6_2();
		result.addObject("queryC6_2", queryC6_2);

		//Query B1
		final String[] queryB1 = this.administratorService.dashboardQueryB1().split(",");
		final String avgQueryB1 = queryB1[0];
		final String minQueryB1 = queryB1[1];
		final String maxQueryB1 = queryB1[2];
		final String stddevQueryB1 = queryB1[3];
		result.addObject("avgQueryB1", avgQueryB1);
		result.addObject("minQueryB1", minQueryB1);
		result.addObject("maxQueryB1", maxQueryB1);
		result.addObject("stddevQueryB1", stddevQueryB1);

		//Query B2
		final String[] queryB2 = this.administratorService.dashboardQueryB2().split(",");
		final String avgQueryB2 = queryB2[0];
		final String minQueryB2 = queryB2[1];
		final String maxQueryB2 = queryB2[2];
		final String stddevQueryB2 = queryB2[3];
		result.addObject("avgQueryB2", avgQueryB2);
		result.addObject("minQueryB2", minQueryB2);
		result.addObject("maxQueryB2", maxQueryB2);
		result.addObject("stddevQueryB2", stddevQueryB2);

		//Query B3
		final String ratioQueryB3 = this.administratorService.dashboardQueryB3();
		result.addObject("ratioQueryB3", ratioQueryB3);

		//Query C1 (Acme-Otaku)
		final String[] queryAcmeRookiesC1 = this.administratorService.dashboardQueryAcmeRookiesC1().split(",");
		final String avgQueryAcmeRookiesC1 = queryAcmeRookiesC1[0];
		final String minQueryAcmeRookiesC1 = queryAcmeRookiesC1[1];
		final String maxQueryAcmeRookiesC1 = queryAcmeRookiesC1[2];
		final String stddevQueryAcmeRookiesC1 = queryAcmeRookiesC1[3];
		result.addObject("avgQueryAcmeRookiesC1", avgQueryAcmeRookiesC1);
		result.addObject("minQueryAcmeRookiesC1", minQueryAcmeRookiesC1);
		result.addObject("maxQueryAcmeRookiesC1", maxQueryAcmeRookiesC1);
		result.addObject("stddevQueryAcmeRookiesC1", stddevQueryAcmeRookiesC1);

		//Query C2 (Acme-Otaku)
		final String[] queryAcmeRookiesC2 = this.administratorService.dashboardQueryAcmeRookiesC2().split(",");
		final String avgQueryAcmeRookiesC2 = queryAcmeRookiesC2[0];
		final String minQueryAcmeRookiesC2 = queryAcmeRookiesC2[1];
		final String maxQueryAcmeRookiesC2 = queryAcmeRookiesC2[2];
		final String stddevQueryAcmeRookiesC2 = queryAcmeRookiesC2[3];
		result.addObject("avgQueryAcmeRookiesC2", avgQueryAcmeRookiesC2);
		result.addObject("minQueryAcmeRookiesC2", minQueryAcmeRookiesC2);
		result.addObject("maxQueryAcmeRookiesC2", maxQueryAcmeRookiesC2);
		result.addObject("stddevQueryAcmeRookiesC2", stddevQueryAcmeRookiesC2);

		//Query C3 (Acme-Otaku)
		final Collection<Company> queryAcmeRookiesC3 = this.administratorService.dashboardQueryAcmeRookiesC3();
		result.addObject("queryAcmeRookiesC3", queryAcmeRookiesC3);

		//Query C4 (Acme-Otaku)
		final String avgQueryAcmeRookiesC4 = this.administratorService.dashboardQueryAcmeRookiesC4();
		result.addObject("avgQueryAcmeRookiesC4", avgQueryAcmeRookiesC4);

		//Query B1 (Acme-Otaku)
		final String[] queryAcmeRookiesB1 = this.administratorService.dashboardQueryAcmeRookiesB1().split(",");
		final String avgQueryAcmeRookiesB1 = queryAcmeRookiesB1[0];
		final String minQueryAcmeRookiesB1 = queryAcmeRookiesB1[1];
		final String maxQueryAcmeRookiesB1 = queryAcmeRookiesB1[2];
		final String stddevQueryAcmeRookiesB1 = queryAcmeRookiesB1[3];
		result.addObject("avgQueryAcmeRookiesB1", avgQueryAcmeRookiesB1);
		result.addObject("minQueryAcmeRookiesB1", minQueryAcmeRookiesB1);
		result.addObject("maxQueryAcmeRookiesB1", maxQueryAcmeRookiesB1);
		result.addObject("stddevQueryAcmeRookiesB1", stddevQueryAcmeRookiesB1);

		//Query B2 (Acme-Otaku)
		final Collection<Provider> queryAcmeRookiesB2 = this.administratorService.dashboardQueryAcmeRookiesB2();
		result.addObject("queryAcmeRookiesB2", queryAcmeRookiesB2);

		//Query A1 (Acme-Otaku)
		final String[] queryAcmeRookiesA1 = this.administratorService.dashboardQueryAcmeRookiesA1().split(",");
		final String avgQueryAcmeRookiesA1 = queryAcmeRookiesA1[0];
		final String minQueryAcmeRookiesA1 = queryAcmeRookiesA1[1];
		final String maxQueryAcmeRookiesA1 = queryAcmeRookiesA1[2];
		final String stddevQueryAcmeRookiesA1 = queryAcmeRookiesA1[3];
		result.addObject("avgQueryAcmeRookiesA1", avgQueryAcmeRookiesA1);
		result.addObject("minQueryAcmeRookiesA1", minQueryAcmeRookiesA1);
		result.addObject("maxQueryAcmeRookiesA1", maxQueryAcmeRookiesA1);
		result.addObject("stddevQueryAcmeRookiesA1", stddevQueryAcmeRookiesA1);

		//Query A2 (Acme-Otaku)
		final String[] queryAcmeRookiesA2 = this.administratorService.dashboardQueryAcmeRookiesA2().split(",");
		final String avgQueryAcmeRookiesA2 = queryAcmeRookiesA2[0];
		final String minQueryAcmeRookiesA2 = queryAcmeRookiesA2[1];
		final String maxQueryAcmeRookiesA2 = queryAcmeRookiesA2[2];
		final String stddevQueryAcmeRookiesA2 = queryAcmeRookiesA2[3];
		result.addObject("avgQueryAcmeRookiesA2", avgQueryAcmeRookiesA2);
		result.addObject("minQueryAcmeRookiesA2", minQueryAcmeRookiesA2);
		result.addObject("maxQueryAcmeRookiesA2", maxQueryAcmeRookiesA2);
		result.addObject("stddevQueryAcmeRookiesA2", stddevQueryAcmeRookiesA2);

		//Query A3 (Acme-Otaku)
		final Collection<Provider> queryAcmeRookiesA3 = this.administratorService.dashboardQueryAcmeRookiesA3();
		result.addObject("queryAcmeRookiesA3", queryAcmeRookiesA3);

		return result;
	}

}
