
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.StandRepository;
import domain.Actor;
import domain.Event;
import domain.Seller;
import domain.Stand;

@Service
@Transactional
public class StandService {

	// Managed repository
	@Autowired
	private StandRepository	standRepository;

	// Supporting services
	@Autowired
	private ActorService	actorService;

	@Autowired
	private EventService	eventService;

	@Autowired
	private SellerService	sellerService;


	// Simple CRUD methods
	//R35.1
	public Stand create() {
		Stand result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSeller(actorLogged);

		final Collection<Event> events = new HashSet<>();

		result = new Stand();
		result.setEvents(events);

		return result;
	}

	//R33.2
	public Collection<Stand> findAll() {
		Collection<Stand> result;

		result = this.standRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Stand findOne(final int standId) {
		Assert.isTrue(standId != 0);

		Stand result;

		result = this.standRepository.findOne(standId);
		Assert.notNull(result);

		return result;
	}

	//R35.1
	public Stand save(final Stand stand) {
		Assert.notNull(stand);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSeller(actorLogged);

		final Seller sellerLogged = (Seller) actorLogged;

		Stand result;

		for (final Event e : stand.getEvents())
			if (stand.getType().equals("COMMERCIAL")) {
				final Collection<Stand> commercialStandsEvent = this.standRepository.findCommercialStandsByEventId(e.getId());
				Assert.isTrue(commercialStandsEvent.size() + 1 <= 5, "The maximum limit of commercial stands is 5 in one of the selected events");
			} else if (stand.getType().equals("ARTISAN")) {
				final Collection<Stand> artisanStandsEvent = this.standRepository.findArtisanStandsByEventId(e.getId());
				Assert.isTrue(artisanStandsEvent.size() + 1 <= 4, "The maximum limit of artisan stands is 4 in one of the selected events");
			} else {
				final Collection<Stand> foodStandsEvent = this.standRepository.findFoodStandsByEventId(e.getId());
				Assert.isTrue(foodStandsEvent.size() + 1 <= 4, "The maximum limit of food stands is 4 in one of the selected events");
			}

		final Collection<Event> events = this.eventService.findEventsFinalModeNotFinished();
		if (!stand.getEvents().isEmpty())
			Assert.isTrue(events.containsAll(stand.getEvents()), "This event is not available to associate it with a stand");

		if (stand.getId() == 0) {
			result = this.standRepository.save(stand);
			final Collection<Stand> standsStandorLogged = sellerLogged.getStands();
			standsStandorLogged.add(result);
			sellerLogged.setStands(standsStandorLogged);
			this.sellerService.saveAuxiliar(sellerLogged);
		} else {
			final Seller sellerOwner = this.sellerService.findSellerByStandId(stand.getId());
			Assert.isTrue(actorLogged.equals(sellerOwner), "The logged actor is not the owner of this entity");
			result = this.standRepository.save(stand);
		}

		return result;
	}

	public Stand saveAuxiliar(final Stand stand) {
		Assert.notNull(stand);

		Stand result;

		result = this.standRepository.save(stand);

		return result;
	}

	//R31.1
	public void delete(final Stand stand) {
		Assert.notNull(stand);
		Assert.isTrue(stand.getId() != 0);
		Assert.isTrue(this.standRepository.exists(stand.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSeller(actorLogged);
		Assert.isTrue(stand.getEvents().isEmpty(), "You can not delete the stand because it has some associated event");

		final Seller sellerOwner = this.sellerService.findSellerByStandId(stand.getId());
		Assert.isTrue(actorLogged.equals(sellerOwner), "The logged actor is not the owner of this entity");

		final Seller sellerLogged = (Seller) actorLogged;

		final Collection<Stand> standsActorLogged = sellerLogged.getStands();
		standsActorLogged.remove(stand);
		sellerLogged.setStands(standsActorLogged);
		this.sellerService.save(sellerLogged);

		this.standRepository.delete(stand);
	}

	public void deleteAuxiliar(final Stand stand) {
		Assert.notNull(stand);
		Assert.isTrue(stand.getId() != 0);
		Assert.isTrue(this.standRepository.exists(stand.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		final Seller sellerLogged = (Seller) actorLogged;

		final Collection<Stand> standsActorLogged = sellerLogged.getStands();
		standsActorLogged.remove(stand);
		sellerLogged.setStands(standsActorLogged);
		this.sellerService.save(sellerLogged);

		this.standRepository.delete(stand);
	}

	// Other business methods
	public Collection<Stand> findStandsByEventId(final int eventId) {
		Assert.isTrue(eventId != 0);

		Collection<Stand> result;

		result = this.standRepository.findStandsByEventId(eventId);

		return result;
	}

	//R31.1
	public Collection<Stand> findStandsBySellerLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSeller(actorLogged);

		Collection<Stand> result;

		final Seller sellerLogged = (Seller) actorLogged;

		result = this.standRepository.findStandsBySellerId(sellerLogged.getId());
		Assert.notNull(result);

		return result;
	}

	public Stand findStandSellerLogged(final int standId) {
		Assert.isTrue(standId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSeller(actorLogged);

		final Seller sellerOwner = this.sellerService.findSellerByStandId(standId);
		Assert.isTrue(actorLogged.equals(sellerOwner), "The logged actor is not the owner of this entity");

		Stand result;

		result = this.standRepository.findOne(standId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Stand reconstruct(final Stand stand, final BindingResult binding) {
		Stand result;

		if (stand.getEvents() == null || stand.getEvents().contains(null)) {
			final Collection<Event> events = new HashSet<>();
			stand.setEvents(events);
		}

		if (stand.getId() == 0)
			result = stand;
		else {
			final Stand originalStand = this.standRepository.findOne(stand.getId());
			Assert.notNull(originalStand, "This entity does not exist");
			result = stand;
		}

		this.validator.validate(result, binding);

		this.standRepository.flush();

		return result;
	}

	public void flush() {
		this.standRepository.flush();
	}

}
