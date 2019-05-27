
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.HeadquarterRepository;
import domain.Actor;
import domain.Headquarter;
import domain.Member;

@Service
@Transactional
public class HeadquarterService {

	// Managed repository
	@Autowired
	private HeadquarterRepository	headquarterRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private MemberService			memberService;


	// Simple CRUD methods
	//R34.2
	public Headquarter create() {
		Headquarter result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association for manage meetings");

		result = new Headquarter();

		return result;
	}

	public Collection<Headquarter> findAll() {
		Collection<Headquarter> result;

		result = this.headquarterRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Headquarter findOne(final int headquarterId) {
		Assert.isTrue(headquarterId != 0);

		Headquarter result;

		result = this.headquarterRepository.findOne(headquarterId);
		Assert.notNull(result);

		return result;
	}

	//R34.2
	public Headquarter save(final Headquarter headquarter) {
		Assert.notNull(headquarter);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		Headquarter result;
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association for manage headquarters");

		if (headquarter.getId() == 0) {
			result = this.headquarterRepository.save(headquarter);
			final Collection<Headquarter> headquartersMemberLogged = memberLogged.getHeadquarters();
			headquartersMemberLogged.add(result);
			memberLogged.setHeadquarters(headquartersMemberLogged);
			this.memberService.saveAuxiliar(memberLogged);
		} else {
			final Member memberOwner = this.memberService.findMemberByHeadquarterId(headquarter.getId());
			Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");
			result = this.headquarterRepository.save(headquarter);
		}

		return result;
	}

	public void delete(final Headquarter headquarter) {
		Assert.notNull(headquarter);
		Assert.isTrue(headquarter.getId() != 0);
		Assert.isTrue(this.headquarterRepository.exists(headquarter.getId()));

		this.headquarterRepository.delete(headquarter);
	}

	public void deleteAuxiliar(final Headquarter headquarter) {
		Assert.notNull(headquarter);
		Assert.isTrue(headquarter.getId() != 0);
		Assert.isTrue(this.headquarterRepository.exists(headquarter.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		final Member memberLogged = (Member) actorLogged;

		final Collection<Headquarter> headquartersActorLogged = memberLogged.getHeadquarters();
		headquartersActorLogged.remove(headquarter);
		memberLogged.setHeadquarters(headquartersActorLogged);
		this.memberService.saveAuxiliar(memberLogged);

		this.headquarterRepository.delete(headquarter);
	}

	// Other business methods
	//R34.2
	public Collection<Headquarter> findHeadquartersByMemberLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Collection<Headquarter> result;

		final Member memberLogged = (Member) actorLogged;
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association for manage meetings");

		result = this.headquarterRepository.findHeadquartersByMemberId(memberLogged.getId());
		Assert.notNull(result);

		return result;
	}

	public Headquarter findHeadquarterMemberLogged(final int headquarterId) {
		Assert.isTrue(headquarterId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberOwner = this.memberService.findMemberByHeadquarterId(headquarterId);
		Assert.isTrue(actorLogged.equals(memberOwner), "The logged actor is not the owner of this entity");

		Headquarter result;

		result = this.headquarterRepository.findOne(headquarterId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Headquarter reconstruct(final Headquarter headquarter, final BindingResult binding) {
		Headquarter result;

		if (headquarter.getId() == 0)
			result = headquarter;
		else {
			final Headquarter originalHeadquarter = this.headquarterRepository.findOne(headquarter.getId());
			Assert.notNull(originalHeadquarter, "This entity does not exist");
			result = headquarter;
		}

		this.validator.validate(result, binding);

		this.headquarterRepository.flush();

		return result;
	}

	public void flush() {
		this.headquarterRepository.flush();
	}

}
