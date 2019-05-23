
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Association;

@Repository
public interface AssociationRepository extends JpaRepository<Association, Integer> {

	@Query("select a from Member m join m.association a where m.id = ?1")
	Association findAssociationByMemberId(int memberId);

	@Query("select a from Association a where a.isAllowedToJoin = 1")
	Collection<Association> findAssociationsToJoin();

}
