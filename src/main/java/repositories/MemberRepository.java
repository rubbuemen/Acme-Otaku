
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {

	@Query("select m from Member m where m.association.id = ?1")
	Collection<Member> findMembersByAssociationId(int associationId);

	@Query("select m from Member m where m.association.id = ?1 and m.role like 'PRESIDENT'")
	Member findMemberPresidentByAssociationId(int associationId);

	@Query("select m from Member m where m.association.id = ?1 and m.role like 'VICEPRESIDENT'")
	Member findMemberVicePresidentByAssociationId(int associationId);

	@Query("select m from Member m join m.events e where e.id = ?1")
	Member findMemberByEventId(int eventId);

	@Query("select m from Member m join m.activities a where a.id = ?1")
	Member findMemberByActivityId(int activityId);

	@Query("select m from Member m join m.meetings me where me.id = ?1")
	Member findMemberByMeetingId(int meetingId);

	@Query("select m from Member m join m.headquarters h where h.id = ?1")
	Member findMemberByHeadquarterId(int headquarterId);

}
