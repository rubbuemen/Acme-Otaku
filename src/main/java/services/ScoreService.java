
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ScoreRepository;
import domain.Score;

@Service
@Transactional
public class ScoreService {

	// Managed repository
	@Autowired
	private ScoreRepository	scoreRepository;


	// Supporting services

	// Simple CRUD methods
	public Score create() {
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

	public Score save(final Score score) {
		Assert.notNull(score);

		Score result;

		if (score.getId() == 0)
			result = this.scoreRepository.save(score);
		else
			result = this.scoreRepository.save(score);

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
