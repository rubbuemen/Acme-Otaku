
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import domain.Score;

@Repository
public interface ScoreRepository extends JpaRepository<Score, Integer> {

}
