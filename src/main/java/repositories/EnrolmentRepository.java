
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Enrolment;

@Repository
public interface EnrolmentRepository extends JpaRepository<Enrolment, Integer> {

	@Query("select e from Member m join m.activities a join a.enrolments e where a.isFinalMode = 1 and m.association.id = ?1")
	Collection<Enrolment> findEnrolmentsByAssociationId(int associationId);

	@Query("select e from Enrolment e join e.visitor v join e.activity a where (e.status like 'PENDING' or e.status like 'ACCEPTED') and v.id = ?1 and a.id = ?2")
	Enrolment findEnrolmentPendingOrAcceptedByVisitorIdActivityId(int visitorId, int activityId);

	@Query("select e from Visitor v join v.enrolments e where v.id = ?1")
	Collection<Enrolment> findEnrolmentsByVisitorId(int visitorId);

}
