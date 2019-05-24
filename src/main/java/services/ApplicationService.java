
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ApplicationRepository;
import domain.Actor;
import domain.Application;
import domain.Association;
import domain.Member;

@Service
@Transactional
public class ApplicationService {

	// Managed repository
	@Autowired
	private ApplicationRepository	applicationRepository;

	// Supporting services
	@Autowired
	private ActorService			actorService;

	@Autowired
	private AssociationService		associationService;

	@Autowired
	private MemberService			memberService;


	// Simple CRUD methods
	//R12.1
	public Application create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;
		Assert.isNull(memberLogged.getAssociation(), "You already belong to an association");

		Application result;

		final Date moment = new Date(System.currentTimeMillis() - 1);

		result = new Application();
		result.setStatus("PENDING");
		result.setMoment(moment);

		result.setMember(memberLogged);

		return result;
	}

	public Collection<Application> findAll() {
		Collection<Application> result;

		result = this.applicationRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Application findOne(final int applicationId) {
		Assert.isTrue(applicationId != 0);

		Application result;

		result = this.applicationRepository.findOne(applicationId);
		Assert.notNull(result);

		return result;
	}

	//R12.1
	public Application save(final Application application) {
		Assert.notNull(application);
		Assert.isTrue(application.getId() == 0, "You can't edit applications");

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;
		Assert.isNull(memberLogged.getAssociation(), "You already belong to an association");

		Application result;

		final Date moment = new Date(System.currentTimeMillis() - 1);

		final Association associationApplication = application.getAssociation();
		final Application alreadyApplicatedPending = this.applicationRepository.findApplicationPendingByMemberIdAssociationId(memberLogged.getId(), associationApplication.getId());

		Assert.isNull(alreadyApplicatedPending, "You already have an application for this association pending");

		final Member memberApplication = (Member) actorLogged;
		Assert.isTrue(this.associationService.findAssociationsToJoin().contains(associationApplication), "You cannot currently join this association");
		application.setMember(memberApplication);
		application.setMoment(moment);
		result = this.applicationRepository.save(application);
		associationApplication.getApplications().add(result);
		this.associationService.saveAuxiliar(associationApplication);
		memberApplication.getApplications().add(result);
		this.memberService.saveAuxiliar(memberApplication);

		return result;
	}
	public Application saveAuxiliar(final Application application) {
		Assert.notNull(application);

		Application result;

		result = this.applicationRepository.save(application);

		return result;
	}

	public void delete(final Application application) {
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		Assert.isTrue(this.applicationRepository.exists(application.getId()));

		this.applicationRepository.delete(application);
	}

	public void deleteAuxiliar(final Application application) {
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		Assert.isTrue(this.applicationRepository.exists(application.getId()));

		this.applicationRepository.delete(application);
	}

	// Other business methods
	public Collection<Application> findApplicationsByMemberLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Collection<Application> result;

		final Member memberLogged = (Member) actorLogged;

		result = this.applicationRepository.findApplicationsByMemberId(memberLogged.getId());
		Assert.notNull(result);

		return result;
	}

	///R13.1
	public Collection<Application> findApplicationsByPresidentLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		Collection<Application> result;

		final Member memberLogged = (Member) actorLogged;
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association to see the applications");

		result = this.applicationRepository.findApplicationsByPresidentId(memberLogged.getId());
		Assert.notNull(result);

		return result;
	}

	//R13.1
	public Application acceptApplication(final Application application) {
		Application result;
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		Assert.isTrue(this.applicationRepository.exists(application.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association to accept an application");
		Assert.isTrue(memberLogged.getAssociation().equals(application.getAssociation()), "These applications do not belong to your association");

		Assert.isTrue(application.getStatus().equals("PENDING"), "The status of this application is not 'pending'");
		application.setStatus("ACCEPTED");

		result = this.applicationRepository.save(application);

		final Member memberApplication = result.getMember();

		final Collection<Application> applicationsToReject = this.applicationRepository.findApplicationsPendingByMemberId(memberApplication.getId());
		for (final Application a : applicationsToReject) {
			a.setStatus("DECLINED");
			this.applicationRepository.save(a);
		}

		memberApplication.setAssociation(result.getAssociation());
		memberApplication.setRole("MEMBER");

		this.memberService.saveAuxiliar(memberApplication);

		return result;
	}

	//R13.1
	public Application declineApplication(final Application application) {
		Application result;
		Assert.notNull(application);
		Assert.isTrue(application.getId() != 0);
		Assert.isTrue(this.applicationRepository.exists(application.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginMember(actorLogged);

		final Member memberLogged = (Member) actorLogged;
		Assert.isTrue(memberLogged.getRole().equals("PRESIDENT"), "You have to be the president of the association to decline an application");
		Assert.isTrue(memberLogged.getAssociation().equals(application.getAssociation()), "These applications do not belong to your association");

		Assert.isTrue(application.getStatus().equals("PENDING"), "The status of this application is not 'pending'");
		application.setStatus("DECLINED");

		result = this.applicationRepository.save(application);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Application reconstruct(final Application application, final BindingResult binding) {
		Application result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);

		final Date moment = new Date(System.currentTimeMillis() - 1);

		//Nunca se edita, siempre se crea
		application.setMoment(moment);
		application.setStatus("PENDING");
		application.setMember((Member) actorLogged);
		result = application;

		this.validator.validate(result, binding);

		this.applicationRepository.flush();

		return result;
	}

	public void flush() {
		this.applicationRepository.flush();
	}

}
