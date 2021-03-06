
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Day;

@Repository
public interface DayRepository extends JpaRepository<Day, Integer> {

}
