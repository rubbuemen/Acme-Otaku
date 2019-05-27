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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ProductService;
import services.StandService;
import services.SystemConfigurationService;
import controllers.AbstractController;
import domain.Product;
import domain.Stand;

@Controller
@RequestMapping("/product/seller")
public class SellerProductController extends AbstractController {

	@Autowired
	ProductService				productService;

	@Autowired
	StandService				standService;

	@Autowired
	SystemConfigurationService	systemConfigurationService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int standId) {
		ModelAndView result;
		Collection<Product> products;
		final Stand stand = this.standService.findOne(standId);

		try {
			final Double vatPercentage = this.systemConfigurationService.getConfiguration().getVATPercentage();
			products = this.productService.findProductsByStandSellerLogged(standId);
			result = new ModelAndView("product/list");
			result.addObject("vatPercentage", vatPercentage);
			result.addObject("products", products);
			result.addObject("requestURI", "product/seller/list.do");
			result.addObject("stand", stand);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(null, "hacking.logged.error", stand);
			else
				result = this.createEditModelAndView(null, "commit.error", stand);
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int standId) {
		ModelAndView result;
		Product product;

		final Stand stand = this.standService.findOne(standId);
		final Collection<Product> products = this.productService.findProductsByStandSellerLogged(standId);
		product = this.productService.create();

		result = this.createEditModelAndView(product, stand);
		result.addObject("products", products);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int standId, @RequestParam final int productId) {
		ModelAndView result;
		Product product = null;
		final Stand stand = this.standService.findOne(standId);

		try {
			this.productService.findProductsByStandSellerLogged(standId);
			product = this.productService.findOne(productId);
			result = this.createEditModelAndView(product, stand);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(product, "hacking.logged.error", stand);
			else
				result = this.createEditModelAndView(product, "commit.error", stand);
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Product product, final BindingResult binding, @RequestParam final int standId) {
		ModelAndView result;

		final Stand stand = this.standService.findOne(standId);

		try {
			product = this.productService.reconstruct(product, binding);
			this.productService.findProductsByStandSellerLogged(standId);
			final Collection<Product> products = this.productService.findProductsByStandSellerLogged(standId);
			if (binding.hasErrors())
				result = this.createEditModelAndView(product, stand);
			else {
				this.productService.save(product, stand);
				result = new ModelAndView("redirect:/product/seller/list.do?standId=" + standId);
			}
			result.addObject("products", products);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(product, "hacking.logged.error", stand);
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error", stand);
			else
				result = this.createEditModelAndView(product, "commit.error", stand);
		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int productId, @RequestParam final int standId) {
		ModelAndView result;

		final Stand stand = this.standService.findOne(standId);
		this.productService.findProductsByStandSellerLogged(standId);
		final Product product = this.productService.findOne(productId);

		try {
			this.productService.delete(product, stand);
			result = new ModelAndView("redirect:/product/seller/list.do?standId=" + standId);
			result.addObject("stand", stand);

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(product, "hacking.logged.error", stand);
			else
				result = this.createEditModelAndView(product, "commit.error", stand);
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Product product, final Stand stand) {
		ModelAndView result;
		result = this.createEditModelAndView(product, null, stand);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Product product, final String message, final Stand stand) {
		ModelAndView result;

		if (product == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (product.getId() == 0)
			result = new ModelAndView("product/create");
		else
			result = new ModelAndView("product/edit");

		final Double vatPercentage = this.systemConfigurationService.getConfiguration().getVATPercentage();

		result.addObject("vatPercentage", vatPercentage);
		result.addObject("stand", stand);
		result.addObject("product", product);
		result.addObject("actionURL", "product/seller/edit.do");
		result.addObject("message", message);

		return result;
	}
}
