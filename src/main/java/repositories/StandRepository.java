
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

	@Query("select distinct s from Stand s join s.events e where s.type like 'COMMERCIAL' and e.id = ?1")
	Collection<Stand> findCommercialStandsByEventId(int eventId);

	@Query("select distinct s from Stand s join s.events e where s.type like 'ARTISAN' and e.id = ?1")
	Collection<Stand> findArtisanStandsByEventId(int eventId);

	@Query("select distinct s from Stand s join s.events e where s.type like 'FOOD' and e.id = ?1")
	Collection<Stand> findFoodStandsByEventId(int eventId);

	@Query("select st from Seller s join s.stands st where s.id = ?1")
	Collection<Stand> findStandsBySellerId(int sellerId);

}
