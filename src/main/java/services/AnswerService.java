
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AnswerRepository;
import domain.Answer;

@Service
@Transactional
public class AnswerService {

	// Managed repository
	@Autowired
	private AnswerRepository	answerRepository;


	// Supporting services

	// Simple CRUD methods
	public Answer create() {
		Answer result;

		result = new Answer();
		return result;
	}

	public Collection<Answer> findAll() {
		Collection<Answer> result;

		result = this.answerRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Answer findOne(final int answerId) {
		Assert.isTrue(answerId != 0);

		Answer result;

		result = this.answerRepository.findOne(answerId);
		Assert.notNull(result);

		return result;
	}

	public Answer save(final Answer answer) {
		Assert.notNull(answer);

		Answer result;

		if (answer.getId() == 0)
			result = this.answerRepository.save(answer);
		else
			result = this.answerRepository.save(answer);

		return result;
	}

	public void delete(final Answer answer) {
		Assert.notNull(answer);
		Assert.isTrue(answer.getId() != 0);
		Assert.isTrue(this.answerRepository.exists(answer.getId()));

		this.answerRepository.delete(answer);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Answer reconstruct(final Answer answer, final BindingResult binding) {
		Answer result;

		if (answer.getId() == 0)
			result = answer;
		else {
			final Answer originalAnswer = this.answerRepository.findOne(answer.getId());
			Assert.notNull(originalAnswer, "This entity does not exist");
			result = answer;
		}

		this.validator.validate(result, binding);

		this.answerRepository.flush();

		return result;
	}

	public void flush() {
		this.answerRepository.flush();
	}

}
