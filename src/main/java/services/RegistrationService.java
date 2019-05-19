
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.RegistrationRepository;
import domain.Registration;

@Service
@Transactional
public class RegistrationService {

	// Managed repository
	@Autowired
	private RegistrationRepository	registrationRepository;


	// Supporting services

	// Simple CRUD methods
	public Registration create() {
		Registration result;

		result = new Registration();
		return result;
	}

	public Collection<Registration> findAll() {
		Collection<Registration> result;

		result = this.registrationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Registration findOne(final int registrationId) {
		Assert.isTrue(registrationId != 0);

		Registration result;

		result = this.registrationRepository.findOne(registrationId);
		Assert.notNull(result);

		return result;
	}

	public Registration save(final Registration registration) {
		Assert.notNull(registration);

		Registration result;

		if (registration.getId() == 0)
			result = this.registrationRepository.save(registration);
		else
			result = this.registrationRepository.save(registration);

		return result;
	}

	public void delete(final Registration registration) {
		Assert.notNull(registration);
		Assert.isTrue(registration.getId() != 0);
		Assert.isTrue(this.registrationRepository.exists(registration.getId()));

		this.registrationRepository.delete(registration);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Registration reconstruct(final Registration registration, final BindingResult binding) {
		Registration result;

		if (registration.getId() == 0)
			result = registration;
		else {
			final Registration originalRegistration = this.registrationRepository.findOne(registration.getId());
			Assert.notNull(originalRegistration, "This entity does not exist");
			result = registration;
		}

		this.validator.validate(result, binding);

		this.registrationRepository.flush();

		return result;
	}

	public void flush() {
		this.registrationRepository.flush();
	}

}
