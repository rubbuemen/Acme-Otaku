
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorRepository;
import domain.Sponsor;

@Service
@Transactional
public class SponsorService {

	// Managed repository
	@Autowired
	private SponsorRepository	sponsorRepository;


	// Supporting services

	// Simple CRUD methods
	public Sponsor create() {
		Sponsor result;

		result = new Sponsor();
		return result;
	}

	public Collection<Sponsor> findAll() {
		Collection<Sponsor> result;

		result = this.sponsorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Sponsor findOne(final int sponsorId) {
		Assert.isTrue(sponsorId != 0);

		Sponsor result;

		result = this.sponsorRepository.findOne(sponsorId);
		Assert.notNull(result);

		return result;
	}

	public Sponsor save(final Sponsor sponsor) {
		Assert.notNull(sponsor);

		Sponsor result;

		if (sponsor.getId() == 0)
			result = this.sponsorRepository.save(sponsor);
		else
			result = this.sponsorRepository.save(sponsor);

		return result;
	}

	public void delete(final Sponsor sponsor) {
		Assert.notNull(sponsor);
		Assert.isTrue(sponsor.getId() != 0);
		Assert.isTrue(this.sponsorRepository.exists(sponsor.getId()));

		this.sponsorRepository.delete(sponsor);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Sponsor reconstruct(final Sponsor sponsor, final BindingResult binding) {
		Sponsor result;

		if (sponsor.getId() == 0)
			result = sponsor;
		else {
			final Sponsor originalSponsor = this.sponsorRepository.findOne(sponsor.getId());
			Assert.notNull(originalSponsor, "This entity does not exist");
			result = sponsor;
		}

		this.validator.validate(result, binding);

		this.sponsorRepository.flush();

		return result;
	}

	public void flush() {
		this.sponsorRepository.flush();
	}

}
