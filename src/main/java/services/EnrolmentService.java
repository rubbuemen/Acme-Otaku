
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EnrolmentRepository;
import domain.Actor;
import domain.Association;
import domain.Enrolment;
import domain.Member;

@Service
@Transactional
public class EnrolmentService {

	// Managed repository
	@Autowired
	private EnrolmentRepository	enrolmentRepository;

	// Supporting services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private AssociationService	associationService;


	// Simple CRUD methods
	public Enrolment create() {
		Enrolment result;

		result = new Enrolment();
		return result;
	}

	public Collection<Enrolment> findAll() {
		Collection<Enrolment> result;

		result = this.enrolmentRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Enrolment findOne(final int enrolmentId) {
		Assert.isTrue(enrolmentId != 0);

		Enrolment result;

		result = this.enrolmentRepository.findOne(enrolmentId);
		Assert.notNull(result);

		return result;
	}

	public Enrolment save(final Enrolment enrolment) {
		Assert.notNull(enrolment);

		Enrolment result;

		if (enrolment.getId() == 0)
			result = this.enrolmentRepository.save(enrolment);
		else
			result = this.enrolmentRepository.save(enrolment);

		return result;
	}

	public void delete(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		Assert.isTrue(enrolment.getId() != 0);
		Assert.isTrue(this.enrolmentRepository.exists(enrolment.getId()));

		this.enrolmentRepository.delete(enrolment);
	}

	public void deleteAuxiliar(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		Assert.isTrue(enrolment.getId() != 0);
		Assert.isTrue(this.enrolmentRepository.exists(enrolment.getId()));

		this.enrolmentRepository.delete(enrolment);
	}

	// Other business methods
	//R14.4
	public Collection<Enrolment> findEnrolmentsByAssociationMemberLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Collection<Enrolment> result;

		final Member memberLogged = (Member) actorLogged;
		Assert.notNull(memberLogged.getAssociation(), "You need to belong to an association to manage activity enrolments");

		result = this.enrolmentRepository.findEnrolmentsByAssociationId(memberLogged.getAssociation().getId());
		Assert.notNull(result);

		return result;
	}

	//R14.4
	public Enrolment acceptEnrolment(final Enrolment enrolment) {
		Enrolment result;
		Assert.notNull(enrolment);
		Assert.isTrue(enrolment.getId() != 0);
		Assert.isTrue(this.enrolmentRepository.exists(enrolment.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;
		final Association associationEnrolment = this.associationService.findAssociationByEnrolmentId(enrolment.getId());
		Assert.isTrue(memberLogged.getAssociation().equals(associationEnrolment), "This enrolment do not belong to your association");

		final Date currentMoment = new Date(System.currentTimeMillis());

		Assert.isTrue(enrolment.getStatus().equals("PENDING"), "The status of this enrolment is not 'pending'");
		Assert.isTrue(enrolment.getActivity().getDeadline().compareTo(currentMoment) > 0, "You can't change the status of this enrolment because the deadline elapsed");
		enrolment.setStatus("ACCEPTED");

		result = this.enrolmentRepository.save(enrolment);

		return result;
	}

	//R14.4
	public Enrolment declineEnrolment(final Enrolment enrolment) {
		Enrolment result;
		Assert.notNull(enrolment);
		Assert.isTrue(enrolment.getId() != 0);
		Assert.isTrue(this.enrolmentRepository.exists(enrolment.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;
		final Association associationEnrolment = this.associationService.findAssociationByEnrolmentId(enrolment.getId());
		Assert.isTrue(memberLogged.getAssociation().equals(associationEnrolment), "This enrolment do not belong to your association");

		final Date currentMoment = new Date(System.currentTimeMillis());

		Assert.isTrue(enrolment.getStatus().equals("PENDING"), "The status of this enrolment is not 'pending'");
		Assert.isTrue(enrolment.getActivity().getDeadline().compareTo(currentMoment) > 0, "You can't change the status of this enrolment because the deadline elapsed");
		enrolment.setStatus("DECLINED");

		result = this.enrolmentRepository.save(enrolment);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Enrolment reconstruct(final Enrolment enrolment, final BindingResult binding) {
		Enrolment result;

		if (enrolment.getId() == 0)
			result = enrolment;
		else {
			final Enrolment originalEnrolment = this.enrolmentRepository.findOne(enrolment.getId());
			Assert.notNull(originalEnrolment, "This entity does not exist");
			result = enrolment;
		}

		this.validator.validate(result, binding);

		this.enrolmentRepository.flush();

		return result;
	}

	public void flush() {
		this.enrolmentRepository.flush();
	}

}
