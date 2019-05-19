
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.DayRepository;
import domain.Day;

@Service
@Transactional
public class DayService {

	// Managed repository
	@Autowired
	private DayRepository	dayRepository;


	// Supporting services

	// Simple CRUD methods
	public Day create() {
		Day result;

		result = new Day();
		return result;
	}

	public Collection<Day> findAll() {
		Collection<Day> result;

		result = this.dayRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Day findOne(final int dayId) {
		Assert.isTrue(dayId != 0);

		Day result;

		result = this.dayRepository.findOne(dayId);
		Assert.notNull(result);

		return result;
	}

	public Day save(final Day day) {
		Assert.notNull(day);

		Day result;

		if (day.getId() == 0)
			result = this.dayRepository.save(day);
		else
			result = this.dayRepository.save(day);

		return result;
	}

	public void delete(final Day day) {
		Assert.notNull(day);
		Assert.isTrue(day.getId() != 0);
		Assert.isTrue(this.dayRepository.exists(day.getId()));

		this.dayRepository.delete(day);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Day reconstruct(final Day day, final BindingResult binding) {
		Day result;

		if (day.getId() == 0)
			result = day;
		else {
			final Day originalDay = this.dayRepository.findOne(day.getId());
			Assert.notNull(originalDay, "This entity does not exist");
			result = day;
		}

		this.validator.validate(result, binding);

		this.dayRepository.flush();

		return result;
	}

	public void flush() {
		this.dayRepository.flush();
	}

}
