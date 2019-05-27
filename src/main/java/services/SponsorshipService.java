
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SponsorshipRepository;
import domain.Actor;
import domain.CreditCard;
import domain.Event;
import domain.Sponsor;
import domain.Sponsorship;

@Service
@Transactional
public class SponsorshipService {

	// Managed repository
	@Autowired
	private SponsorshipRepository	sponsorshipRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private SponsorService			sponsorService;

	@Autowired
	private EventService			eventService;


	// Simple CRUD methods
	//R46.1
	public Sponsorship create() {
		Sponsorship result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSponsor(actorLogged);

		result = new Sponsorship();

		result.setSponsor((Sponsor) actorLogged);

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

	//R46.1
	public Sponsorship save(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSponsor(actorLogged);

		final Sponsor sponsorLogged = (Sponsor) actorLogged;

		Sponsorship result;

		if (!sponsorship.getCreditCard().getNumber().isEmpty())
			Assert.isTrue(this.isNumeric(sponsorship.getCreditCard().getNumber()), "Invalid credit card");
		if (sponsorship.getCreditCard().getExpirationYear() != null && sponsorship.getCreditCard().getExpirationMonth() != null && sponsorship.getCreditCard().getExpirationYear() >= 0)
			Assert.isTrue(this.checkCreditCard(sponsorship.getCreditCard()), "Expired credit card");

		Assert.isTrue(sponsorship.getEvent().getIsFinalMode(), "To sponsor a event, it must be in final mode");

		if (sponsorship.getId() == 0) {
			result = this.sponsorshipRepository.save(sponsorship);
			final Event eventSponsorship = result.getEvent();
			final Collection<Sponsorship> sponsorshipsEvent = eventSponsorship.getSponsorships();
			sponsorshipsEvent.add(result);
			eventSponsorship.setSponsorships(sponsorshipsEvent);
			this.eventService.saveAuxiliar(eventSponsorship);
			final Collection<Sponsorship> sponsorshipsSponsor = sponsorLogged.getSponsorships();
			sponsorshipsSponsor.add(result);
			sponsorLogged.setSponsorships(sponsorshipsSponsor);
			this.sponsorService.save(sponsorLogged);
		} else {
			final Sponsor sponsorOwner = this.sponsorService.findSponsorBySponsorshipId(sponsorship.getId());
			Assert.isTrue(actorLogged.equals(sponsorOwner), "The logged actor is not the owner of this entity");
			result = this.sponsorshipRepository.save(sponsorship);
		}

		return result;
	}

	//R46.1
	public void delete(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(sponsorship.getId() != 0);
		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSponsor(actorLogged);

		final Sponsor sponsorLogged = (Sponsor) actorLogged;

		final Sponsor sponsorOwner = this.sponsorService.findSponsorBySponsorshipId(sponsorship.getId());
		Assert.isTrue(actorLogged.equals(sponsorOwner), "The logged actor is not the owner of this entity");

		final Event eventSponsorship = sponsorship.getEvent();
		final Collection<Sponsorship> sponsorshipsEvent = eventSponsorship.getSponsorships();
		sponsorshipsEvent.remove(sponsorship);
		eventSponsorship.setSponsorships(sponsorshipsEvent);
		this.eventService.saveAuxiliar(eventSponsorship);
		final Collection<Sponsorship> sponsorshipsSponsor = sponsorLogged.getSponsorships();
		sponsorshipsSponsor.remove(sponsorship);
		sponsorLogged.setSponsorships(sponsorshipsSponsor);
		this.sponsorService.save(sponsorLogged);

		this.sponsorshipRepository.delete(sponsorship);
	}

	public void deleteAuxiliar(final Sponsorship sponsorship) {
		Assert.notNull(sponsorship);
		Assert.isTrue(sponsorship.getId() != 0);
		Assert.isTrue(this.sponsorshipRepository.exists(sponsorship.getId()));

		this.sponsorshipRepository.delete(sponsorship);
	}

	// Other business methods
	//R46.1
	public Collection<Sponsorship> findSponsorshipsBySponsorLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSponsor(actorLogged);

		Collection<Sponsorship> result;

		final Sponsor sponsorLogged = (Sponsor) actorLogged;

		result = this.sponsorshipRepository.findSponsorshipsBySponsorId(sponsorLogged.getId());
		Assert.notNull(result);

		return result;
	}

	public Sponsorship findSponsorshipSponsorLogged(final int sponsorshipId) {
		Assert.isTrue(sponsorshipId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSponsor(actorLogged);

		final Sponsor sponsorOwner = this.sponsorService.findSponsorBySponsorshipId(sponsorshipId);
		Assert.isTrue(actorLogged.equals(sponsorOwner), "The logged actor is not the owner of this entity");

		Sponsorship result;

		result = this.sponsorshipRepository.findOne(sponsorshipId);
		Assert.notNull(result);

		return result;
	}

	public boolean isNumeric(final String cadena) {

		boolean resultado;

		try {
			Long.parseLong(cadena);
			resultado = true;
		} catch (final NumberFormatException excepcion) {
			resultado = false;
		}

		return resultado;
	}

	public boolean checkCreditCard(final CreditCard creditCard) {
		boolean result;
		Calendar calendar;
		int actualYear, actualMonth;

		result = false;
		calendar = new GregorianCalendar();
		actualYear = calendar.get(Calendar.YEAR);
		actualMonth = calendar.get(Calendar.MONTH) + 1;
		actualYear = actualYear % 100;
		if (creditCard.getExpirationYear() > actualYear)
			result = true;
		else if (creditCard.getExpirationYear() == actualYear && creditCard.getExpirationMonth() >= actualMonth)
			result = true;
		return result;
	}

	public Collection<Sponsorship> findSponsorshipsByEventId(final int eventId) {
		final Collection<Sponsorship> result;

		result = this.sponsorshipRepository.findSponsorshipsByEventId(eventId);
		Assert.notNull(result);

		return result;
	}

	//R48
	public Sponsorship findRandomSponsorship(final int eventId) {
		Sponsorship result = null;

		final Random r = new Random();
		final Collection<Sponsorship> sponsorships = this.findSponsorshipsByEventId(eventId);
		if (!sponsorships.isEmpty()) {
			final int i = r.nextInt(sponsorships.size());
			result = (Sponsorship) sponsorships.toArray()[i];
		}

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Sponsorship reconstruct(final Sponsorship sponsorship, final BindingResult binding) {
		Sponsorship result;

		if (sponsorship.getId() == 0) {
			final Actor actorLogged = this.actorService.findActorLogged();
			sponsorship.setSponsor((Sponsor) actorLogged);
			result = sponsorship;
		} else {
			final Sponsorship originalSponsorship = this.sponsorshipRepository.findOne(sponsorship.getId());
			Assert.notNull(originalSponsorship, "This entity does not exist");
			result = sponsorship;
			result.setSponsor(originalSponsorship.getSponsor());
			result.setEvent(originalSponsorship.getEvent());
		}

		this.validator.validate(result, binding);

		return result;
	}
	public void flush() {
		this.sponsorshipRepository.flush();
	}

}
