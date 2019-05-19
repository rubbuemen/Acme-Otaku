
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SocialProfileRepository;
import domain.SocialProfile;

@Service
@Transactional
public class SocialProfileService {

	// Managed repository
	@Autowired
	private SocialProfileRepository	socialprofileRepository;


	// Supporting services

	// Simple CRUD methods
	public SocialProfile create() {
		SocialProfile result;

		result = new SocialProfile();
		return result;
	}

	public Collection<SocialProfile> findAll() {
		Collection<SocialProfile> result;

		result = this.socialprofileRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public SocialProfile findOne(final int socialprofileId) {
		Assert.isTrue(socialprofileId != 0);

		SocialProfile result;

		result = this.socialprofileRepository.findOne(socialprofileId);
		Assert.notNull(result);

		return result;
	}

	public SocialProfile save(final SocialProfile socialprofile) {
		Assert.notNull(socialprofile);

		SocialProfile result;

		if (socialprofile.getId() == 0)
			result = this.socialprofileRepository.save(socialprofile);
		else
			result = this.socialprofileRepository.save(socialprofile);

		return result;
	}

	public void delete(final SocialProfile socialprofile) {
		Assert.notNull(socialprofile);
		Assert.isTrue(socialprofile.getId() != 0);
		Assert.isTrue(this.socialprofileRepository.exists(socialprofile.getId()));

		this.socialprofileRepository.delete(socialprofile);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public SocialProfile reconstruct(final SocialProfile socialprofile, final BindingResult binding) {
		SocialProfile result;

		if (socialprofile.getId() == 0)
			result = socialprofile;
		else {
			final SocialProfile originalSocialProfile = this.socialprofileRepository.findOne(socialprofile.getId());
			Assert.notNull(originalSocialProfile, "This entity does not exist");
			result = socialprofile;
		}

		this.validator.validate(result, binding);

		this.socialprofileRepository.flush();

		return result;
	}

	public void flush() {
		this.socialprofileRepository.flush();
	}

}
