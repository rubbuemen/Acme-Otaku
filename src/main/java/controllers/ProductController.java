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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ProductService;
import services.SystemConfigurationService;
import domain.Product;

@Controller
@RequestMapping("/product")
public class ProductController extends AbstractController {

	@Autowired
	ProductService				productService;

	@Autowired
	SystemConfigurationService	systemConfigurationService;


	@RequestMapping(value = "/listGeneric", method = RequestMethod.GET)
	public ModelAndView listProducts(@RequestParam final int standId) {
		ModelAndView result;
		Collection<Product> products;

		products = this.productService.findProductsByStandId(standId);

		final Double vatPercentage = this.systemConfigurationService.getConfiguration().getVATPercentage();

		result = new ModelAndView("product/listGeneric");

		result.addObject("products", products);
		result.addObject("vatPercentage", vatPercentage);
		result.addObject("requestURI", "product/listGeneric.do");

		return result;
	}

}
