
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	// Managed repository
	@Autowired
	private SponsorshipRepository	sponsorshipRepository;


	// Supporting services

	// Simple CRUD methods
	public Sponsorship create() {
		Sponsorship result;

		result = new Sponsorship();
		return result;
	}

	public Collection<Sponsorship> findAll() {
		Collection<Sponsorship> result;

		result = this.sponsorshipRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Sponsorship findOne(final int sponsorshipId) {
		Assert.isTrue(sponsorshipId != 0);

		Sponsorship result;

		result = this.sponsorshipRepository.findOne(sponsorshipId);
		Assert.notNull(result);

		return result;
	}

	public Sponsorship save(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);

		Sponsorship result;

		if (sponsorship.getId() == 0)
			result = this.sponsorshipRepository.save(sponsorship);
		else
			result = this.sponsorshipRepository.save(sponsorship);

		return result;
	}

	public void delete(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(sponsorship.getId() != 0);
		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));

		this.sponsorshipRepository.delete(sponsorship);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Sponsorship reconstruct(final Sponsorship sponsorship, final BindingResult binding) {
		Sponsorship result;

		if (sponsorship.getId() == 0)
			result = sponsorship;
		else {
			final Sponsorship originalSponsorship = this.sponsorshipRepository.findOne(sponsorship.getId());
			Assert.notNull(originalSponsorship, "This entity does not exist");
			result = sponsorship;
		}

		this.validator.validate(result, binding);

		this.sponsorshipRepository.flush();

		return result;
	}

	public void flush() {
		this.sponsorshipRepository.flush();
	}

}
