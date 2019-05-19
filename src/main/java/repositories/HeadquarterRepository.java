
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Headquarter;

@Repository
public interface HeadquarterRepository extends JpaRepository<Headquarter, Integer> {

}
