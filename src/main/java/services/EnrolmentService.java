
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EnrolmentRepository;
import domain.Enrolment;

@Service
@Transactional
public class EnrolmentService {

	// Managed repository
	@Autowired
	private EnrolmentRepository	enrolmentRepository;


	// Supporting services

	// Simple CRUD methods
	public Enrolment create() {
		Enrolment result;

		result = new Enrolment();
		return result;
	}

	public Collection<Enrolment> findAll() {
		Collection<Enrolment> result;

		result = this.enrolmentRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Enrolment findOne(final int enrolmentId) {
		Assert.isTrue(enrolmentId != 0);

		Enrolment result;

		result = this.enrolmentRepository.findOne(enrolmentId);
		Assert.notNull(result);

		return result;
	}

	public Enrolment save(final Enrolment enrolment) {
		Assert.notNull(enrolment);

		Enrolment result;

		if (enrolment.getId() == 0)
			result = this.enrolmentRepository.save(enrolment);
		else
			result = this.enrolmentRepository.save(enrolment);

		return result;
	}

	public void delete(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		Assert.isTrue(enrolment.getId() != 0);
		Assert.isTrue(this.enrolmentRepository.exists(enrolment.getId()));

		this.enrolmentRepository.delete(enrolment);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Enrolment reconstruct(final Enrolment enrolment, final BindingResult binding) {
		Enrolment result;

		if (enrolment.getId() == 0)
			result = enrolment;
		else {
			final Enrolment originalEnrolment = this.enrolmentRepository.findOne(enrolment.getId());
			Assert.notNull(originalEnrolment, "This entity does not exist");
			result = enrolment;
		}

		this.validator.validate(result, binding);

		this.enrolmentRepository.flush();

		return result;
	}

	public void flush() {
		this.enrolmentRepository.flush();
	}

}
