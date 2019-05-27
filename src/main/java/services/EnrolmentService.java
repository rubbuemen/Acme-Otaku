
package services;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.EnrolmentRepository;
import domain.Activity;
import domain.Actor;
import domain.Association;
import domain.Enrolment;
import domain.Member;
import domain.Message;
import domain.Visitor;

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

	@Autowired
	private ActivityService		activityService;

	@Autowired
	private VisitorService		visitorService;

	@Autowired
	private MemberService		memberService;

	@Autowired
	private MessageService		messageService;


	// Simple CRUD methods
	//R15.4
	public Enrolment create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		final Visitor visitorLogged = (Visitor) actorLogged;

		Enrolment result;

		final Date moment = new Date(System.currentTimeMillis() - 1);

		result = new Enrolment();
		result.setStatus("PENDING");
		result.setMoment(moment);

		result.setVisitor(visitorLogged);

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

	//R15.4
	public Enrolment save(final Enrolment enrolment) {
		Assert.notNull(enrolment);
		Assert.isTrue(enrolment.getId() == 0, "You can't edit enrolments");

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		final Visitor visitorLogged = (Visitor) actorLogged;

		Enrolment result;

		final Date moment = new Date(System.currentTimeMillis() - 1);

		final Activity activityEnrolment = enrolment.getActivity();
		final Enrolment alreadyApplicated = this.enrolmentRepository.findEnrolmentPendingOrAcceptedByVisitorIdActivityId(visitorLogged.getId(), activityEnrolment.getId());

		Assert.isNull(alreadyApplicated, "You already have an enrolment for this activity pending or accepted");

		final Visitor visitorEnrolment = (Visitor) actorLogged;
		Assert.isTrue(this.activityService.findActivitiesAvailables().contains(activityEnrolment), "This activity is not available for enrolment");
		enrolment.setVisitor(visitorEnrolment);
		enrolment.setMoment(moment);
		result = this.enrolmentRepository.save(enrolment);
		activityEnrolment.getEnrolments().add(result);
		this.activityService.saveAuxiliar(activityEnrolment);
		visitorEnrolment.getEnrolments().add(result);
		this.visitorService.saveAuxiliar(visitorEnrolment);

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

		// R23
		final Visitor visitor = enrolment.getVisitor();

		final Message message = this.messageService.createAuxiliar();

		message.setSubject("A enrolment to an activity has been accepted //// Una inscripción a una actividad ha sido aceptada");
		message.setBody("Enrolment for the activity '" + enrolment.getActivity().getName() + "' has been accepted //// La inscripción a la actividad '" + enrolment.getActivity().getName() + "' ha sido aceptada");

		final Actor sender = this.actorService.getSystemActor();
		message.setPriority("HIGH");
		message.setSender(sender);

		final Collection<Actor> recipients = new HashSet<>();
		recipients.add(visitor);
		message.setRecipients(recipients);
		this.messageService.save(message, true);

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

		// R23
		final Visitor visitor = enrolment.getVisitor();

		final Message message = this.messageService.createAuxiliar();

		message.setSubject("A enrolment to an activity has been declined //// Una inscripción a una actividad ha sido rechazada");
		message.setBody("Enrolment for the activity '" + enrolment.getActivity().getName() + "' has been declined //// La inscripción a la actividad '" + enrolment.getActivity().getName() + "' ha sido rechazada");

		final Actor sender = this.actorService.getSystemActor();
		message.setPriority("HIGH");
		message.setSender(sender);

		final Collection<Actor> recipients = new HashSet<>();
		recipients.add(visitor);
		message.setRecipients(recipients);
		this.messageService.save(message, true);

		return result;
	}

	public Collection<Enrolment> findEnrolmentsByVisitorLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		Collection<Enrolment> result;

		final Visitor visitorLogged = (Visitor) actorLogged;

		result = this.enrolmentRepository.findEnrolmentsByVisitorId(visitorLogged.getId());
		Assert.notNull(result);

		return result;
	}

	//R15.4
	public Enrolment cancelEnrolment(final Enrolment enrolment) {
		Enrolment result;
		Assert.notNull(enrolment);
		Assert.isTrue(enrolment.getId() != 0);
		Assert.isTrue(this.enrolmentRepository.exists(enrolment.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		final Visitor visitorOwner = this.visitorService.findVisitorByEnrolmentId(enrolment.getId());
		Assert.isTrue(actorLogged.equals(visitorOwner), "The logged actor is not the owner of this entity");

		final Date currentMoment = new Date(System.currentTimeMillis());

		Assert.isTrue(enrolment.getStatus().equals("PENDING") || enrolment.getStatus().equals("ACCEPTED"), "The status of this enrolment is not 'pending' or 'accepted'");
		Assert.isTrue(enrolment.getActivity().getDeadline().compareTo(currentMoment) > 0, "You can't change the status of this enrolment because the deadline elapsed");
		enrolment.setStatus("CANCELLED");

		// R23
		final Member member = this.memberService.findMemberByActivityId(enrolment.getActivity().getId());

		final Message message = this.messageService.createAuxiliar();

		message.setSubject("A enrolment to an activity has been cancelled //// Una inscripción a una actividad ha sido cancelada");
		message.setBody("Enrolment for the activity '" + enrolment.getActivity().getName() + "' has been cancelled //// La inscripción a la actividad '" + enrolment.getActivity().getName() + "' ha sido cancelada");

		final Actor sender = this.actorService.getSystemActor();
		message.setPriority("HIGH");
		message.setSender(sender);

		final Collection<Actor> recipients = new HashSet<>();
		recipients.add(member);
		message.setRecipients(recipients);
		this.messageService.save(message, true);

		result = this.enrolmentRepository.save(enrolment);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Enrolment reconstruct(final Enrolment enrolment, final BindingResult binding) {
		Enrolment result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		final Date moment = new Date(System.currentTimeMillis() - 1);

		//Nunca se edita, siempre se crea
		enrolment.setMoment(moment);
		enrolment.setStatus("PENDING");
		enrolment.setVisitor((Visitor) actorLogged);
		result = enrolment;

		this.validator.validate(result, binding);

		this.enrolmentRepository.flush();

		return result;
	}

	public void flush() {
		this.enrolmentRepository.flush();
	}

}
