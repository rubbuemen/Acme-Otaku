
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Meeting;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Integer> {

	@Query("select me from Member m join m.meetings me where m.id = ?1")
	Collection<Meeting> findMeetingsByMemberId(int memberId);

}
