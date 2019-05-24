
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

}
