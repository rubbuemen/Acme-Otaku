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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.AdministratorService;
import services.CategoryService;
import controllers.AbstractController;
import domain.Category;

@Controller
@RequestMapping("/category/administrator")
public class AdministratorCategoryController extends AbstractController {

	@Autowired
	CategoryService			categoryService;

	@Autowired
	AdministratorService	administratorService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;
		Collection<Category> categories;

		categories = this.categoryService.findAll();
		final Collection<Category> categoriesUsed = this.categoryService.findPositionsBrotherhoodUsed();

		result = new ModelAndView("category/list");

		result.addObject("categories", categories);
		result.addObject("categoriesUsed", categoriesUsed);
		result.addObject("requestURI", "category/administrator/list.do");

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Category category;

		category = this.categoryService.create();

		result = this.createEditModelAndView(category);

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int categoryId) {
		ModelAndView result;
		Category category = null;

		try {
			category = this.categoryService.findOne(categoryId);
			result = this.createEditModelAndView(category);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(category, "hacking.logged.error");
			else
				result = this.createEditModelAndView(category, "commit.error");
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Category category, final BindingResult binding) {
		ModelAndView result;

		try {
			category = this.categoryService.reconstruct(category, binding);
			if (binding.hasErrors())
				result = this.createEditModelAndView(category);
			else {
				this.categoryService.save(category);
				result = new ModelAndView("redirect:/category/administrator/list.do");
			}
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("could not execute statement; SQL [n/a]; constraint [null]" + "; nested exception is org.hibernate.exception.ConstraintViolationException: could not execute statement"))
				result = this.createEditModelAndView(category, "category.error.duplicate.name");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(category, "hacking.logged.error");
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error");
			else
				result = this.createEditModelAndView(category, "commit.error");

		}

		return result;
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int categoryId) {
		ModelAndView result;

		final Category category = this.categoryService.findOne(categoryId);

		try {
			this.categoryService.delete(category);
			result = new ModelAndView("redirect:/category/administrator/list.do");

		} catch (final Throwable oops) {
			if (oops.getMessage().equals("This category can not be deleted because it is in use"))
				result = this.createEditModelAndView(category, "category.error.occupied");
			else if (oops.getMessage().equals("The logged actor is not the owner of this entity"))
				result = this.createEditModelAndView(category, "hacking.logged.error");
			else
				result = this.createEditModelAndView(category, "commit.error");
		}

		return result;
	}

	// Ancillary methods

	protected ModelAndView createEditModelAndView(final Category category) {
		ModelAndView result;
		result = this.createEditModelAndView(category, null);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Category category, final String message) {
		ModelAndView result;

		if (category == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (category.getId() == 0)
			result = new ModelAndView("category/create");
		else
			result = new ModelAndView("category/edit");

		result.addObject("category", category);
		result.addObject("actionURL", "category/administrator/edit.do");
		result.addObject("message", message);

		return result;
	}

}
