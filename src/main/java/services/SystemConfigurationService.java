
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SystemConfigurationRepository;
import domain.Actor;
import domain.SystemConfiguration;

@Service
@Transactional
public class SystemConfigurationService {

	// Managed repository
	@Autowired
	private SystemConfigurationRepository	systemConfigurationRepository;

	// Supporting services
	@Autowired
	private ActorService					actorService;


	// Simple CRUD methods
	public SystemConfiguration create() {
		SystemConfiguration result;

		result = new SystemConfiguration();
		return result;
	}

	public Collection<SystemConfiguration> findAll() {
		Collection<SystemConfiguration> result;

		result = this.systemConfigurationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public SystemConfiguration findOne(final int systemConfigurationId) {
		Assert.isTrue(systemConfigurationId != 0);

		SystemConfiguration result;

		result = this.systemConfigurationRepository.findOne(systemConfigurationId);
		Assert.notNull(result);

		return result;
	}

	public SystemConfiguration save(final SystemConfiguration systemConfiguration) {
		Assert.notNull(systemConfiguration);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginAdministrator(actorLogged);

		SystemConfiguration result;

		result = this.systemConfigurationRepository.save(systemConfiguration);

		return result;
	}

	public void delete(final SystemConfiguration systemConfiguration) {
		Assert.notNull(systemConfiguration);
		Assert.isTrue(systemConfiguration.getId() != 0);
		Assert.isTrue(this.systemConfigurationRepository.exists(systemConfiguration.getId()));

		this.systemConfigurationRepository.delete(systemConfiguration);
	}

	// Other business methods
	public SystemConfiguration getConfiguration() {
		SystemConfiguration result;

		result = this.systemConfigurationRepository.getConfiguration();
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public SystemConfiguration reconstruct(final SystemConfiguration systemConfiguration, final BindingResult binding) {
		SystemConfiguration result;

		if (systemConfiguration.getId() == 0)
			result = systemConfiguration;
		else {
			final SystemConfiguration originalSystemConfiguration = this.systemConfigurationRepository.findOne(systemConfiguration.getId());
			Assert.notNull(originalSystemConfiguration, "This entity does not exist");
			result = systemConfiguration;
		}

		this.validator.validate(result, binding);

		this.systemConfigurationRepository.flush();

		return result;
	}

	public void flush() {
		this.systemConfigurationRepository.flush();
	}

}
