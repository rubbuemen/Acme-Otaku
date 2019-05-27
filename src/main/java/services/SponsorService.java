
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorRepository;
import security.Authority;
import security.UserAccount;
import domain.Actor;
import domain.Box;
import domain.Event;
import domain.Sponsor;
import domain.Sponsorship;
import forms.SponsorForm;

@Service
@Transactional
public class SponsorService {

	// Managed repository
	@Autowired
	private SponsorRepository	sponsorRepository;

	// Supporting services
	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private EventService		eventService;

	@Autowired
	private SponsorshipService	sponsorshipService;


	// Simple CRUD methods
	// R44.1
	public Sponsor create() {
		Sponsor result;

		result = new Sponsor();
		final Collection<Sponsorship> sponsorships = new HashSet<>();
		final Collection<Box> boxes = new HashSet<>();
		final UserAccount userAccount = this.userAccountService.create();
		final Authority auth = new Authority();

		auth.setAuthority(Authority.SPONSOR);
		userAccount.addAuthority(auth);
		result.setBoxes(boxes);
		result.setSponsorships(sponsorships);
		result.setUserAccount(userAccount);
		result.setIsSuspicious(false);

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

	// R44.1
	public Sponsor save(final Sponsor sponsor) {
		Assert.notNull(sponsor);

		Sponsor result;

		result = (Sponsor) this.actorService.save(sponsor);
		result = this.sponsorRepository.save(result);

		return result;
	}

	public Sponsor saveAuxiliar(final Sponsor sponsor) {
		Assert.notNull(sponsor);

		Sponsor result;

		result = this.sponsorRepository.save(sponsor);

		return result;
	}

	public void delete(final Sponsor sponsor) {
		Assert.notNull(sponsor);
		Assert.isTrue(sponsor.getId() != 0);
		Assert.isTrue(this.sponsorRepository.exists(sponsor.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSponsor(actorLogged);

		final Sponsor sponsorLogged = (Sponsor) actorLogged;

		this.actorService.deleteEntities(sponsorLogged);

		final Collection<Sponsorship> sponsorships = new HashSet<>(sponsorLogged.getSponsorships());
		for (final Sponsorship ss : sponsorships) {
			final Event e = ss.getEvent();
			sponsorLogged.getSponsorships().remove(ss);
			e.getSponsorships().remove(ss);
			this.eventService.saveAuxiliar(e);
			this.sponsorshipService.deleteAuxiliar(ss);
		}

		this.sponsorRepository.flush();
		this.sponsorRepository.delete(sponsor);
	}

	// Other business methods
	public Sponsor findSponsorBySponsorshipId(final int sponsorshipId) {
		Sponsor result;

		result = this.sponsorRepository.findSponsorBySponsorshipId(sponsorshipId);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public SponsorForm reconstruct(final SponsorForm sponsorForm, final BindingResult binding) {
		SponsorForm result;
		final Sponsor sponsor = sponsorForm.getActor();

		if (sponsor.getId() == 0) {
			final Collection<Box> boxes = new HashSet<>();
			final Collection<Sponsorship> sponsorships = new HashSet<>();
			final UserAccount userAccount = this.userAccountService.create();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.SPONSOR);
			userAccount.addAuthority(auth);
			userAccount.setUsername(sponsorForm.getActor().getUserAccount().getUsername());
			userAccount.setPassword(sponsorForm.getActor().getUserAccount().getPassword());
			sponsor.setSponsorships(sponsorships);
			sponsor.setUserAccount(userAccount);
			sponsor.setIsSuspicious(false);
			sponsor.setBoxes(boxes);
			sponsorForm.setActor(sponsor);
		} else {
			final Sponsor res = this.sponsorRepository.findOne(sponsor.getId());
			Assert.notNull(res, "This entity does not exist");
			res.setName(sponsor.getName());
			res.setMiddleName(sponsor.getMiddleName());
			res.setSurname(sponsor.getSurname());
			res.setPhoto(sponsor.getPhoto());
			res.setEmail(sponsor.getEmail());
			res.setPhoneNumber(sponsor.getPhoneNumber());
			res.setAddress(sponsor.getAddress());
			sponsorForm.setActor(res);
		}

		result = sponsorForm;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.sponsorRepository.flush();
	}

}
