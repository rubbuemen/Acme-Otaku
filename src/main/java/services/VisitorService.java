
package services;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.VisitorRepository;
import security.Authority;
import security.UserAccount;
import domain.Activity;
import domain.Actor;
import domain.Box;
import domain.Enrolment;
import domain.Report;
import domain.Visitor;
import forms.VisitorForm;

@Service
@Transactional
public class VisitorService {

	// Managed repository
	@Autowired
	private VisitorRepository	visitorRepository;

	// Supporting services
	@Autowired
	private UserAccountService	userAccountService;

	@Autowired
	private ActorService		actorService;

	@Autowired
	private EnrolmentService	enrolmentService;

	@Autowired
	private ReportService		reportService;


	// Simple CRUD methods
	// R10.1
	public Visitor create() {
		Visitor result;

		result = new Visitor();
		final Collection<Report> reports = new HashSet<>();
		final Collection<Enrolment> enrolments = new HashSet<>();
		final Collection<Box> boxes = new HashSet<>();
		final UserAccount userAccount = this.userAccountService.create();
		final Authority auth = new Authority();

		auth.setAuthority(Authority.VISITOR);
		userAccount.addAuthority(auth);
		result.setBoxes(boxes);
		result.setReports(reports);
		result.setEnrolments(enrolments);
		result.setUserAccount(userAccount);
		result.setIsSuspicious(false);

		return result;
	}

	public Collection<Visitor> findAll() {
		Collection<Visitor> result;

		result = this.visitorRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Visitor findOne(final int visitorId) {
		Assert.isTrue(visitorId != 0);

		Visitor result;

		result = this.visitorRepository.findOne(visitorId);
		Assert.notNull(result);

		return result;
	}

	// R10.1
	public Visitor save(final Visitor visitor) {
		Assert.notNull(visitor);

		Visitor result;

		result = (Visitor) this.actorService.save(visitor);
		result = this.visitorRepository.save(result);

		return result;
	}

	public Visitor saveAuxiliar(final Visitor visitor) {
		Assert.notNull(visitor);

		Visitor result;

		result = this.visitorRepository.save(visitor);

		return result;
	}

	public void delete(final Visitor visitor) {
		Assert.notNull(visitor);
		Assert.isTrue(visitor.getId() != 0);
		Assert.isTrue(this.visitorRepository.exists(visitor.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		final Visitor visitorLogged = (Visitor) actorLogged;

		this.actorService.deleteEntities(visitorLogged);

		final Collection<Enrolment> enrolments = new HashSet<>(visitorLogged.getEnrolments());
		for (final Enrolment e : enrolments) {
			final Activity a = e.getActivity();
			a.getEnrolments().remove(e);
			visitorLogged.getEnrolments().remove(e);
			this.enrolmentService.delete(e);
		}

		final Collection<Report> reportsVisitor = new HashSet<>(visitorLogged.getReports());
		for (final Report r : reportsVisitor) {
			visitorLogged.getReports().remove(r);
			this.reportService.deleteAuxiliar(r);
			this.visitorRepository.save(visitorLogged);
		}

		this.visitorRepository.flush();
		this.visitorRepository.delete(visitor);
	}

	// Other business methods
	public Visitor findVisitorByReportId(final int reportId) {
		Visitor result;

		result = this.visitorRepository.findVisitorByReportId(reportId);

		return result;
	}

	public Visitor findVisitorByEnrolmentId(final int enrolmentId) {
		Visitor result;

		result = this.visitorRepository.findVisitorByEnrolmentId(enrolmentId);

		return result;
	}

	public Collection<Visitor> findVisitorsByActivityId(final int activityId) {
		Collection<Visitor> result;

		result = this.visitorRepository.findVisitorsByActivityId(activityId);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public VisitorForm reconstruct(final VisitorForm visitorForm, final BindingResult binding) {
		VisitorForm result;
		final Visitor visitor = visitorForm.getActor();

		if (visitor.getId() == 0) {
			final Collection<Box> boxes = new HashSet<>();
			final Collection<Report> reports = new HashSet<>();
			final Collection<Enrolment> enrolments = new HashSet<>();
			final UserAccount userAccount = this.userAccountService.create();
			final Authority auth = new Authority();
			auth.setAuthority(Authority.VISITOR);
			userAccount.addAuthority(auth);
			userAccount.setUsername(visitorForm.getActor().getUserAccount().getUsername());
			userAccount.setPassword(visitorForm.getActor().getUserAccount().getPassword());
			visitor.setReports(reports);
			visitor.setEnrolments(enrolments);
			visitor.setUserAccount(userAccount);
			visitor.setIsSuspicious(false);
			visitor.setBoxes(boxes);
			visitorForm.setActor(visitor);
		} else {
			final Visitor res = this.visitorRepository.findOne(visitor.getId());
			Assert.notNull(res, "This entity does not exist");
			res.setName(visitor.getName());
			res.setMiddleName(visitor.getMiddleName());
			res.setSurname(visitor.getSurname());
			res.setPhoto(visitor.getPhoto());
			res.setEmail(visitor.getEmail());
			res.setPhoneNumber(visitor.getPhoneNumber());
			res.setAddress(visitor.getAddress());
			visitorForm.setActor(res);
		}

		result = visitorForm;

		this.validator.validate(result, binding);

		return result;
	}

	public void flush() {
		this.visitorRepository.flush();
	}

}
