
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.StandRepository;
import domain.Stand;

@Service
@Transactional
public class StandService {

	// Managed repository
	@Autowired
	private StandRepository	standRepository;


	// Supporting services

	// Simple CRUD methods
	public Stand create() {
		Stand result;

		result = new Stand();
		return result;
	}

	public Collection<Stand> findAll() {
		Collection<Stand> result;

		result = this.standRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Stand findOne(final int standId) {
		Assert.isTrue(standId != 0);

		Stand result;

		result = this.standRepository.findOne(standId);
		Assert.notNull(result);

		return result;
	}

	public Stand save(final Stand stand) {
		Assert.notNull(stand);

		Stand result;

		if (stand.getId() == 0)
			result = this.standRepository.save(stand);
		else
			result = this.standRepository.save(stand);

		return result;
	}

	public void delete(final Stand stand) {
		Assert.notNull(stand);
		Assert.isTrue(stand.getId() != 0);
		Assert.isTrue(this.standRepository.exists(stand.getId()));

		this.standRepository.delete(stand);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Stand reconstruct(final Stand stand, final BindingResult binding) {
		Stand result;

		if (stand.getId() == 0)
			result = stand;
		else {
			final Stand originalStand = this.standRepository.findOne(stand.getId());
			Assert.notNull(originalStand, "This entity does not exist");
			result = stand;
		}

		this.validator.validate(result, binding);

		this.standRepository.flush();

		return result;
	}

	public void flush() {
		this.standRepository.flush();
	}

}
