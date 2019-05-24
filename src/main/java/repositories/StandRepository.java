
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Stand;

@Repository
public interface StandRepository extends JpaRepository<Stand, Integer> {

	@Query("select distinct s from Stand s join s.events e where e.id = ?1")
	Collection<Stand> findStandsByEventId(int eventId);

}
