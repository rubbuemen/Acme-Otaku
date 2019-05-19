
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.BoxRepository;
import domain.Box;

@Service
@Transactional
public class BoxService {

	// Managed repository
	@Autowired
	private BoxRepository	boxRepository;


	// Supporting services

	// Simple CRUD methods
	public Box create() {
		Box result;

		result = new Box();
		return result;
	}

	public Collection<Box> findAll() {
		Collection<Box> result;

		result = this.boxRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Box findOne(final int boxId) {
		Assert.isTrue(boxId != 0);

		Box result;

		result = this.boxRepository.findOne(boxId);
		Assert.notNull(result);

		return result;
	}

	public Box save(final Box box) {
		Assert.notNull(box);

		Box result;

		if (box.getId() == 0)
			result = this.boxRepository.save(box);
		else
			result = this.boxRepository.save(box);

		return result;
	}

	public void delete(final Box box) {
		Assert.notNull(box);
		Assert.isTrue(box.getId() != 0);
		Assert.isTrue(this.boxRepository.exists(box.getId()));

		this.boxRepository.delete(box);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Box reconstruct(final Box box, final BindingResult binding) {
		Box result;

		if (box.getId() == 0)
			result = box;
		else {
			final Box originalBox = this.boxRepository.findOne(box.getId());
			Assert.notNull(originalBox, "This entity does not exist");
			result = box;
		}

		this.validator.validate(result, binding);

		this.boxRepository.flush();

		return result;
	}

	public void flush() {
		this.boxRepository.flush();
	}

}
