
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ActorRepository;
import security.Authority;
import security.LoginService;
import security.UserAccount;
import domain.Actor;

@Service
@Transactional
public class ActorService {

	// Managed repository
	@Autowired
	private ActorRepository	actorRepository;


	// Supporting services

	// Simple CRUD methods
	public Collection<Actor> findAll() {
		Collection<Actor> result;

		result = this.actorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Actor findOne(final int actorId) {
		Assert.isTrue(actorId != 0);

		Actor result;

		result = this.actorRepository.findOne(actorId);
		Assert.notNull(result);

		return result;
	}

	public Actor save(final Actor actor) {
		Assert.notNull(actor);

		Actor result;

		if (actor.getId() == 0)
			result = this.actorRepository.save(actor);
		else
			result = this.actorRepository.save(actor);

		return result;
	}

	public void delete(final Actor actor) {
		Assert.notNull(actor);
		Assert.isTrue(actor.getId() != 0);
		Assert.isTrue(this.actorRepository.exists(actor.getId()));

		this.actorRepository.delete(actor);
	}

	// Other business methods
	public Actor findActorLogged() {
		Actor result;
		UserAccount userAccount;

		userAccount = LoginService.getPrincipal();
		Assert.notNull(userAccount);

		result = this.actorRepository.findActorByUserAccountId(userAccount.getId());
		Assert.notNull(result);

		return result;
	}

	public Actor getSystemActor() {
		Actor result;

		result = this.actorRepository.getSystemActor();
		Assert.notNull(result);

		return result;
	}

	public Actor getDeletedActor() {
		Actor result;

		result = this.actorRepository.getDeletedActor();
		Assert.notNull(result);

		return result;
	}

	public void checkUserLoginMember(final Actor actor) {
		final Authority auth = new Authority();
		auth.setAuthority(Authority.MEMBER);
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.contains(auth), "The logged actor is not a member");
	}

	public void checkUserLoginVisitor(final Actor actor) {
		final Authority auth = new Authority();
		auth.setAuthority(Authority.VISITOR);
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.contains(auth), "The logged actor is not a visitor");
	}

	public void checkUserLoginAdministrator(final Actor actor) {
		final Authority auth = new Authority();
		auth.setAuthority(Authority.ADMIN);
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.contains(auth), "The logged actor is not an administrator");
	}

	public void checkUserLoginSeller(final Actor actor) {
		final Authority auth = new Authority();
		auth.setAuthority(Authority.SELLER);
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.contains(auth), "The logged actor is not a seller");
	}

	public void checkUserLoginSponsor(final Actor actor) {
		final Authority auth = new Authority();
		auth.setAuthority(Authority.SPONSOR);
		final Collection<Authority> authorities = actor.getUserAccount().getAuthorities();
		Assert.isTrue(authorities.contains(auth), "The logged actor is not a sponsor");
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Actor reconstruct(final Actor actor, final BindingResult binding) {
		Actor result;

		if (actor.getId() == 0)
			result = actor;
		else {
			final Actor originalActor = this.actorRepository.findOne(actor.getId());
			Assert.notNull(originalActor, "This entity does not exist");
			result = actor;
		}

		this.validator.validate(result, binding);

		this.actorRepository.flush();

		return result;
	}

	public void flush() {
		this.actorRepository.flush();
	}

}
