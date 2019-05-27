
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Headquarter;

@Repository
public interface HeadquarterRepository extends JpaRepository<Headquarter, Integer> {

	@Query("select h from Member m join m.headquarters h where m.id = ?1")
	Collection<Headquarter> findHeadquartersByMemberId(int memberId);

}
