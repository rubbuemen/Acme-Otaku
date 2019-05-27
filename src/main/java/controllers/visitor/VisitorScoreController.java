/*
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package controllers.visitor;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActivityService;
import services.ScoreService;
import controllers.AbstractController;
import domain.Activity;
import domain.Score;

@Controller
@RequestMapping("/score/visitor")
public class VisitorScoreController extends AbstractController {

	@Autowired
	ScoreService	scoreService;

	@Autowired
	ActivityService	activityService;


	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list(@RequestParam final int activityId) {
		ModelAndView result;
		Collection<Score> scores;
		final Activity activity = this.activityService.findOne(activityId);

		try {
			scores = this.scoreService.findScoresByActivityId(activityId);
			result = new ModelAndView("score/list");
			result.addObject("scores", scores);
			result.addObject("requestURI", "score/visitor/list.do");
			result.addObject("activity", activity);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have not participated in this activity"))
				result = this.createEditModelAndView(null, "score.error.notParticipated", activity);
			else
				result = this.createEditModelAndView(null, "commit.error", activity);
		}

		return result;
	}

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create(@RequestParam final int activityId) {
		ModelAndView result;
		Score score;
		Collection<Score> scores;
		final Activity activity = this.activityService.findOne(activityId);

		try {
			scores = this.scoreService.findScoresByActivityId(activityId);
			score = this.scoreService.create();
			result = this.createEditModelAndView(score, activity);
			result.addObject("scores", scores);
			result.addObject("requestURI", "score/visitor/list.do");
			result.addObject("activity", activity);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have not participated in this activity"))
				result = this.createEditModelAndView(null, "score.error.notParticipated", activity);
			else
				result = this.createEditModelAndView(null, "commit.error", activity);
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int activityId, @RequestParam final int scoreId) {
		ModelAndView result;
		Score score = null;
		final Activity activity = this.activityService.findOne(activityId);

		try {
			this.scoreService.findScoresByActivityId(activityId);
			score = this.scoreService.findOne(scoreId);
			result = this.createEditModelAndView(score, activity);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have not participated in this activity"))
				result = this.createEditModelAndView(score, "score.error.notParticipated", activity);
			else
				result = this.createEditModelAndView(score, "commit.error", activity);
		}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView createOrEdit(Score score, final BindingResult binding, @RequestParam final int activityId) {
		ModelAndView result;

		final Activity activity = this.activityService.findOne(activityId);

		try {
			score = this.scoreService.reconstruct(score, binding);
			final Collection<Score> scores = this.scoreService.findScoresByActivityId(activityId);
			if (binding.hasErrors())
				result = this.createEditModelAndView(score, activity);
			else {
				this.scoreService.save(score, activity);
				result = new ModelAndView("redirect:/score/visitor/list.do?activityId=" + activityId);
			}
			result.addObject("scores", scores);
		} catch (final Throwable oops) {
			if (oops.getMessage().equals("You have not participated in this activity"))
				result = this.createEditModelAndView(score, "score.error.notParticipated", activity);
			else if (oops.getMessage().equals("The activity is not finished yet"))
				result = this.createEditModelAndView(score, "score.error.notFinished", activity);
			else if (oops.getMessage().equals("This entity does not exist"))
				result = this.createEditModelAndView(null, "hacking.notExist.error", activity);
			else
				result = this.createEditModelAndView(score, "commit.error", activity);
		}

		return result;
	}

	// Ancillary methods
	protected ModelAndView createEditModelAndView(final Score score, final Activity activity) {
		ModelAndView result;
		result = this.createEditModelAndView(score, null, activity);
		return result;
	}

	protected ModelAndView createEditModelAndView(final Score score, final String message, final Activity activity) {
		ModelAndView result;

		if (score == null)
			result = new ModelAndView("redirect:/welcome/index.do");
		else if (score.getId() == 0)
			result = new ModelAndView("score/create");
		else
			result = new ModelAndView("score/edit");

		result.addObject("activity", activity);
		result.addObject("score", score);
		result.addObject("actionURL", "score/visitor/edit.do");
		result.addObject("message", message);

		return result;
	}
}
