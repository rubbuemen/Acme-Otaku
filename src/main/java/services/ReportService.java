
package services;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ReportRepository;
import domain.Actor;
import domain.Report;
import domain.Visitor;

@Service
@Transactional
public class ReportService {

	// Managed repository
	@Autowired
	private ReportRepository	reportRepository;

	// Supporting services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private VisitorService		visitorService;


	// Simple CRUD methods
	//R45.1 
	public Report create() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		Report result;

		result = new Report();

		final Date moment = new Date(System.currentTimeMillis() - 1);

		result.setMoment(moment);

		return result;
	}

	//R44.2
	public Collection<Report> findAll() {
		Collection<Report> result;

		result = this.reportRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Report findOne(final int reportId) {
		Assert.isTrue(reportId != 0);

		Report result;

		result = this.reportRepository.findOne(reportId);
		Assert.notNull(result);

		return result;
	}

	//R45.1 
	public Report save(final Report report) {
		Assert.notNull(report);

		Report result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		final Visitor visitorLogged = (Visitor) actorLogged;

		if (report.getId() == 0) {
			final Date moment = new Date(System.currentTimeMillis() - 1);
			report.setMoment(moment);
			result = this.reportRepository.save(report);
			final Collection<Report> reportsVisitorLogged = visitorLogged.getReports();
			reportsVisitorLogged.add(result);
			visitorLogged.setReports(reportsVisitorLogged);
			this.visitorService.save(visitorLogged);
		} else {
			final Visitor visitorOwner = this.visitorService.findVisitorByReportId(report.getId());
			Assert.isTrue(actorLogged.equals(visitorOwner), "The logged actor is not the owner of this entity");
			result = this.reportRepository.save(report);
		}

		return result;
	}

	//R45.1 
	public void delete(final Report report) {
		Assert.notNull(report);
		Assert.isTrue(report.getId() != 0);
		Assert.isTrue(this.reportRepository.exists(report.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		final Visitor visitorOwner = this.visitorService.findVisitorByReportId(report.getId());
		Assert.isTrue(actorLogged.equals(visitorOwner), "The logged actor is not the owner of this entity");

		final Visitor visitorLogged = (Visitor) actorLogged;

		final Collection<Report> reportsActorLogged = visitorLogged.getReports();
		reportsActorLogged.remove(report);
		visitorLogged.setReports(reportsActorLogged);
		this.visitorService.save(visitorLogged);

		this.reportRepository.delete(report);
	}

	public void deleteAuxiliar(final Report report) {
		Assert.notNull(report);
		Assert.isTrue(report.getId() != 0);
		Assert.isTrue(this.reportRepository.exists(report.getId()));

		this.reportRepository.delete(report);
	}

	// Other business methods
	//R45.1 
	public Collection<Report> findReportsByVisitorLogged() {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		Collection<Report> result;

		final Visitor visitorLogged = (Visitor) actorLogged;

		result = this.reportRepository.findReportsByVisitorId(visitorLogged.getId());
		Assert.notNull(result);

		return result;
	}

	public Report findReportVisitorLogged(final int reportId) {
		Assert.isTrue(reportId != 0);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginVisitor(actorLogged);

		final Visitor visitorOwner = this.visitorService.findVisitorByReportId(reportId);
		Assert.isTrue(actorLogged.equals(visitorOwner), "The logged actor is not the owner of this entity");

		Report result;

		result = this.reportRepository.findOne(reportId);
		Assert.notNull(result);

		return result;
	}

	public Collection<Report> findReportsByStandId(final int standId) {
		Collection<Report> result;

		result = this.reportRepository.findReportsByStandId(standId);
		Assert.notNull(result);

		return result;
	}


	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Report reconstruct(final Report report, final BindingResult binding) {
		Report result;

		if (report.getId() == 0) {
			final Date moment = new Date(System.currentTimeMillis() - 1);
			report.setMoment(moment);
			result = report;
		}

		else {
			final Report originalReport = this.reportRepository.findOne(report.getId());
			Assert.notNull(originalReport, "This entity does not exist");
			result = report;
			result.setMoment(originalReport.getMoment());
			result.setStand(originalReport.getStand());
		}

		this.validator.validate(result, binding);

		this.reportRepository.flush();

		return result;
	}

	public void flush() {
		this.reportRepository.flush();
	}

}
