
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Activity;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Integer> {

	@Query("select a from Member m join m.activities a where a.isFinalMode = 1 and a.isFinished = 0 and m.association.id = ?1")
	Collection<Activity> findActivitiesFinalModeByAssociationId(int associationId);

	@Query("select a from Member m join m.activities a where m.id = ?1")
	Collection<Activity> findActivitiesByMemberId(int memberId);

	@Query("select a from Event e join e.activities a where a.isFinalMode = 1 and a.isFinished = 0 and e.id = ?1")
	Collection<Activity> findActivitiesFinalModeNotFinishedByEventId(int eventId);

}
