
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Association;

@Repository
public interface AssociationRepository extends JpaRepository<Association, Integer> {

}
