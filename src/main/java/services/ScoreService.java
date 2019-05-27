
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ScoreRepository;
import domain.Activity;
import domain.Actor;
import domain.Score;
import domain.Visitor;

@Service
@Transactional
public class ScoreService {

	// Managed repository
	@Autowired
	private ScoreRepository	scoreRepository;

	// Supporting services
	@Autowired
	private ActorService	actorService;

	@Autowired
	private VisitorService	visitorService;

	@Autowired
	private ActivityService	activityService;


	// Simple CRUD methods
	//R15.4
	public Score create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		Score result;

		result = new Score();
		return result;
	}

	public Collection<Score> findAll() {
		Collection<Score> result;

		result = this.scoreRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Score findOne(final int scoreId) {
		Assert.isTrue(scoreId != 0);

		Score result;

		result = this.scoreRepository.findOne(scoreId);
		Assert.notNull(result);

		return result;
	}

	//R15.4
	public Score save(final Score score, final Activity activity) {
		Assert.notNull(score);
		Assert.isTrue(score.getId() == 0, "You can't edit scores");

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		Score result;

		final Collection<Visitor> visitorParticipated = this.visitorService.findVisitorsByActivityId(activity.getId());
		Assert.isTrue(visitorParticipated.contains(actorLogged), "You have not participated in this activity");

		Assert.isTrue(activity.getIsFinished(), "The activity is not finished yet");

		result = this.scoreRepository.save(score);
		final Collection<Score> scoresActivity = activity.getScores();
		scoresActivity.add(result);
		activity.setScores(scoresActivity);
		this.activityService.saveAuxiliar(activity);

		return result;
	}

	public void delete(final Score score) {
		Assert.notNull(score);
		Assert.isTrue(score.getId() != 0);
		Assert.isTrue(this.scoreRepository.exists(score.getId()));

		this.scoreRepository.delete(score);
	}

	public void deleteAuxiliar(final Score score) {
		Assert.notNull(score);
		Assert.isTrue(score.getId() != 0);
		Assert.isTrue(this.scoreRepository.exists(score.getId()));

		this.scoreRepository.delete(score);
	}

	// Other business methods
	public Collection<Score> findScoresByActivityId(final int activityId) {
		Assert.isTrue(activityId != 0);

		Collection<Score> result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		final Collection<Visitor> visitorParticipated = this.visitorService.findVisitorsByActivityId(activityId);
		Assert.isTrue(visitorParticipated.contains(actorLogged), "You have not participated in this activity");

		final Activity activity = this.activityService.findOne(activityId);
		result = activity.getScores();

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Score reconstruct(final Score score, final BindingResult binding) {
		Score result;

		if (score.getId() == 0)
			result = score;
		else {
			final Score originalScore = this.scoreRepository.findOne(score.getId());
			Assert.notNull(originalScore, "This entity does not exist");
			result = score;
		}

		this.validator.validate(result, binding);

		this.scoreRepository.flush();

		return result;
	}

	public void flush() {
		this.scoreRepository.flush();
	}

}
