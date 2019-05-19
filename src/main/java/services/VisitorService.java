
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.VisitorRepository;
import domain.Visitor;

@Service
@Transactional
public class VisitorService {

	// Managed repository
	@Autowired
	private VisitorRepository	visitorRepository;


	// Supporting services

	// Simple CRUD methods
	public Visitor create() {
		Visitor result;

		result = new Visitor();
		return result;
	}

	public Collection<Visitor> findAll() {
		Collection<Visitor> result;

		result = this.visitorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Visitor findOne(final int visitorId) {
		Assert.isTrue(visitorId != 0);

		Visitor result;

		result = this.visitorRepository.findOne(visitorId);
		Assert.notNull(result);

		return result;
	}

	public Visitor save(final Visitor visitor) {
		Assert.notNull(visitor);

		Visitor result;

		if (visitor.getId() == 0)
			result = this.visitorRepository.save(visitor);
		else
			result = this.visitorRepository.save(visitor);

		return result;
	}

	public void delete(final Visitor visitor) {
		Assert.notNull(visitor);
		Assert.isTrue(visitor.getId() != 0);
		Assert.isTrue(this.visitorRepository.exists(visitor.getId()));

		this.visitorRepository.delete(visitor);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Visitor reconstruct(final Visitor visitor, final BindingResult binding) {
		Visitor result;

		if (visitor.getId() == 0)
			result = visitor;
		else {
			final Visitor originalVisitor = this.visitorRepository.findOne(visitor.getId());
			Assert.notNull(originalVisitor, "This entity does not exist");
			result = visitor;
		}

		this.validator.validate(result, binding);

		this.visitorRepository.flush();

		return result;
	}

	public void flush() {
		this.visitorRepository.flush();
	}

}
