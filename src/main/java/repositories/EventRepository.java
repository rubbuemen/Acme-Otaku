
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Event;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

	@Query("select e from Member m join m.events e where m.id = ?1")
	Collection<Event> findEventsByMemberId(int memberId);

	@Query("select distinct e from Event e join e.days d where e.isFinalMode = 1 and d.date > CURRENT_DATE")
	Collection<Event> findEventsFinalModeNotFinished();

}
