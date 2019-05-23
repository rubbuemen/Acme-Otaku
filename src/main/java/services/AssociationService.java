
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.AssociationRepository;
import domain.Actor;
import domain.Application;
import domain.Association;
import domain.Member;

@Service
@Transactional
public class AssociationService {

	// Managed repository
	@Autowired
	private AssociationRepository	associationRepository;

	// Supporting services
	@Autowired
	private MemberService			memberService;

	@Autowired
	private ActorService			actorService;


	// Simple CRUD methods
	//R12.1
	public Association create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;
		Assert.isNull(memberLogged.getAssociation(), "You already belong to an association");

		final Collection<Application> applications = new HashSet<>();

		Association result;

		result = new Association();

		result.setApplications(applications);
		result.setIsAllowedToJoin(false);

		return result;
	}

	public Collection<Association> findAll() {
		Collection<Association> result;

		result = this.associationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Association findOne(final int associationId) {
		Assert.isTrue(associationId != 0);

		Association result;

		result = this.associationRepository.findOne(associationId);
		Assert.notNull(result);

		return result;
	}

	//R12.1, R13.2
	public Association save(final Association association) {
		Assert.notNull(association);

		Association result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		if (association.getId() == 0) {
			Assert.isNull(memberLogged.getAssociation(), "You already belong to an association");
			result = this.associationRepository.save(association);
			memberLogged.setAssociation(result);
			memberLogged.setRole("PRESIDENT");
			this.memberService.saveAuxiliar(memberLogged);
		} else {
			Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association in order to edit it");
			Assert.isTrue(memberLogged.getAssociation().equals(association), "You don't belong to this association");
			result = this.associationRepository.save(association);
		}

		return result;
	}

	public Association saveAuxiliar(final Association association) {
		Assert.notNull(association);

		Association result;

		result = this.associationRepository.save(association);

		return result;
	}

	public void delete(final Association association) {
		Assert.notNull(association);
		Assert.isTrue(association.getId() != 0);
		Assert.isTrue(this.associationRepository.exists(association.getId()));

		this.associationRepository.delete(association);
	}

	// Other business methods
	public Association findAssociationByMemberLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Association result;

		final Member memberLogged = (Member) actorLogged;

		result = this.associationRepository.findAssociationByMemberId(memberLogged.getId());

		return result;
	}

	public Association findAssociationMemberLogged(final int associationId) {
		Assert.isTrue(associationId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		final Association association = this.associationRepository.findOne(associationId);

		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association in order to edit it");
		Assert.isTrue(memberLogged.getAssociation().equals(association), "You don't belong to this association");

		Association result;

		result = this.associationRepository.findOne(associationId);
		Assert.notNull(result);

		return result;
	}

	//R13.2
	public Association allowMembers(final Association association) {
		Association result;
		Assert.notNull(association);
		Assert.isTrue(association.getId() != 0);
		Assert.isTrue(this.associationRepository.exists(association.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		Assert.isTrue(!association.getIsAllowedToJoin(), "This association already allows members");
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association in order to edit it");
		Assert.isTrue(memberLogged.getAssociation().equals(association), "You don't belong to this association");

		association.setIsAllowedToJoin(true);

		result = this.associationRepository.save(association);

		return result;
	}

	//R13.2
	public Association notAllowMembers(final Association association) {
		Association result;
		Assert.notNull(association);
		Assert.isTrue(association.getId() != 0);
		Assert.isTrue(this.associationRepository.exists(association.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;

		Assert.isTrue(association.getIsAllowedToJoin(), "This association no longer allows members");
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association in order to edit it");
		Assert.isTrue(memberLogged.getAssociation().equals(association), "You don't belong to this association");

		association.setIsAllowedToJoin(false);

		result = this.associationRepository.save(association);

		return result;
	}

	//R13.1
	public Collection<Association> findAssociations() {
		Collection<Association> result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		result = this.associationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Collection<Association> findAssociationsToJoin() {
		Collection<Association> result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		result = this.associationRepository.findAssociationsToJoin();
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Association reconstruct(final Association association, final BindingResult binding) {
		Association result;

		if (association.getId() == 0) {
			final Collection<Application> applications = new HashSet<>();
			association.setApplications(applications);
			association.setIsAllowedToJoin(false);
			result = association;
		} else {
			final Association originalAssociation = this.associationRepository.findOne(association.getId());
			Assert.notNull(originalAssociation, "This entity does not exist");
			result = association;
			result.setApplications(originalAssociation.getApplications());
			result.setIsAllowedToJoin(originalAssociation.getIsAllowedToJoin());
		}

		this.validator.validate(result, binding);

		this.associationRepository.flush();

		return result;
	}

	public void flush() {
		this.associationRepository.flush();
	}

}
