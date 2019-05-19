
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AssociationRepository;
import domain.Association;

@Service
@Transactional
public class AssociationService {

	// Managed repository
	@Autowired
	private AssociationRepository	associationRepository;


	// Supporting services

	// Simple CRUD methods
	public Association create() {
		Association result;

		result = new Association();
		return result;
	}

	public Collection<Association> findAll() {
		Collection<Association> result;

		result = this.associationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Association findOne(final int associationId) {
		Assert.isTrue(associationId != 0);

		Association result;

		result = this.associationRepository.findOne(associationId);
		Assert.notNull(result);

		return result;
	}

	public Association save(final Association association) {
		Assert.notNull(association);

		Association result;

		if (association.getId() == 0)
			result = this.associationRepository.save(association);
		else
			result = this.associationRepository.save(association);

		return result;
	}

	public void delete(final Association association) {
		Assert.notNull(association);
		Assert.isTrue(association.getId() != 0);
		Assert.isTrue(this.associationRepository.exists(association.getId()));

		this.associationRepository.delete(association);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Association reconstruct(final Association association, final BindingResult binding) {
		Association result;

		if (association.getId() == 0)
			result = association;
		else {
			final Association originalAssociation = this.associationRepository.findOne(association.getId());
			Assert.notNull(originalAssociation, "This entity does not exist");
			result = association;
		}

		this.validator.validate(result, binding);

		this.associationRepository.flush();

		return result;
	}

	public void flush() {
		this.associationRepository.flush();
	}

}
