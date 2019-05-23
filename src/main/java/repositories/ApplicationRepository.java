
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Application;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

	@Query("select a from Application a join a.member m join a.association ass where a.status like 'PENDING' and m.id = ?1 and ass.id = ?2")
	Application findApplicationPendingByMemberIdAssociationId(int memberId, int associationId);

	@Query("select a from Application a join a.member m where m.id = ?1")
	Collection<Application> findApplicationsByMemberId(int memberId);

	@Query("select ap from Member m join m.association a join a.applications ap where m.id = ?1")
	Collection<Application> findApplicationsByPresidentId(int presidentId);

	@Query("select a from Application a join a.member m where a.status like 'PENDING' and m.id = ?1")
	Collection<Application> findApplicationsPendingByMemberId(int memberId);

}
