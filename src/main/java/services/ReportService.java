
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ReportRepository;
import domain.Report;

@Service
@Transactional
public class ReportService {

	// Managed repository
	@Autowired
	private ReportRepository	reportRepository;


	// Supporting services

	// Simple CRUD methods
	public Report create() {
		Report result;

		result = new Report();
		return result;
	}

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

	public Report save(final Report report) {
		Assert.notNull(report);

		Report result;

		if (report.getId() == 0)
			result = this.reportRepository.save(report);
		else
			result = this.reportRepository.save(report);

		return result;
	}

	public void delete(final Report report) {
		Assert.notNull(report);
		Assert.isTrue(report.getId() != 0);
		Assert.isTrue(this.reportRepository.exists(report.getId()));

		this.reportRepository.delete(report);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Report reconstruct(final Report report, final BindingResult binding) {
		Report result;

		if (report.getId() == 0)
			result = report;
		else {
			final Report originalReport = this.reportRepository.findOne(report.getId());
			Assert.notNull(originalReport, "This entity does not exist");
			result = report;
		}

		this.validator.validate(result, binding);

		this.reportRepository.flush();

		return result;
	}

	public void flush() {
		this.reportRepository.flush();
	}

}
