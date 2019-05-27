
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Integer> {

	@Query("select r from Visitor v join v.reports r where v.id = ?1")
	Collection<Report> findReportsByVisitorId(int visitorId);

	@Query("select r from Report r join r.stand s where s.id = ?1")
	Collection<Report> findReportsByStandId(int standId);

}
