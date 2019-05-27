
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Sponsorship;

@Repository
public interface SponsorshipRepository extends JpaRepository<Sponsorship, Integer> {

	@Query("select sp from Sponsor s join s.sponsorships sp where s.id = ?1")
	Collection<Sponsorship> findSponsorshipsBySponsorId(int sponsorId);

	@Query("select sp from Event e join e.sponsorships sp where e.id = ?1")
	Collection<Sponsorship> findSponsorshipsByEventId(int eventId);

}
