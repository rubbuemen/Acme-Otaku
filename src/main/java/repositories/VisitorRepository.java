
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Visitor;

@Repository
public interface VisitorRepository extends JpaRepository<Visitor, Integer> {

	@Query("select v from Visitor v join v.reports r where r.id = ?1")
	Visitor findVisitorByReportId(int reportId);

	@Query("select v from Visitor v join v.enrolments e where e.id = ?1")
	Visitor findVisitorByEnrolmentId(int enrolmentId);

	@Query("select distinct v from Visitor v join v.enrolments e join e.activity a where e.status like 'ACCEPTED' and a.id = ?1")
	Collection<Visitor> findVisitorsByActivityId(int activityId);

}
