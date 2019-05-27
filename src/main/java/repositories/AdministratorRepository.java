
package repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Administrator;
import domain.Seller;
import domain.Stand;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Integer> {

	@Query("select avg(m.activities.size), min(m.activities.size), max(m.activities.size), stddev(m.activities.size) from Member m")
	String dashboardQueryC1();

	@Query("select avg(a.enrolments.size), min(a.enrolments.size), max(a.enrolments.size), stddev(a.enrolments.size) from Activity a where a.isFinalMode = 1")
	String dashboardQueryC2();

	@Query("select avg(1.0*(select sum(d.price) from Event e join e.days d where e.id = ev.id)), min(1.0*(select sum(d.price) from Event e join e.days d where e.id = ev.id)), max(1.0*(select sum(d.price) from Event e join e.days d where e.id = ev.id)), stddev(1.0*(select sum(d.price) from Event e join e.days d where e.id = ev.id)) from Event ev")
	String dashboardQueryC3();

	@Query("select 1.0*count(e1)/(select count(e2) from Enrolment e2) from Enrolment e1 where e1.status = 'DECLINED'")
	String dashboardQueryC4();

	@Query("select 1.0*count(e1)/(select count(e2) from Enrolment e2) from Enrolment e1 where e1.status = 'PENDING'")
	String dashboardQueryC5();

	@Query("select 1.0*count(e1)/(select count(e2) from Enrolment e2) from Enrolment e1 where e1.status = 'ACCEPTED'")
	String dashboardQueryC6();

	@Query("select 1.0*count(e1)/(select count(e2) from Enrolment e2) from Enrolment e1 where e1.status = 'CANCELLED'")
	String dashboardQueryC7();

	@Query("select 1.0*count(e1)/(select count(e2) from Enrolment e2) from Enrolment e1 where (e1.status = 'PENDING' or e1.status = 'ACCEPTED') and e1.activity.deadline < CURRENT_DATE")
	String dashboardQueryC8();

	@Query("select avg(m.meetings.size), min(m.meetings.size), max(m.meetings.size), stddev(m.meetings.size) from Member m")
	String dashboardQueryB1();

	@Query("select avg(1.0*(select count(e) from Stand s join s.events e where e.id=ev.id)), min(1*(select count(e) from Stand s join s.events e where e.id=ev.id)), max(1*(select count(e) from Stand s join s.events e where e.id=ev.id)), stddev(1.0*(select count(e) from Stand s join s.events e where e.id=ev.id)) from Event ev")
	String dashboardQueryB2();

	@Query("select 1.0*count(s1)/(select count(s2) from Stand s2) from Stand s1 where s1.products.size > 3")
	String dashboardQueryB3();

	@Query("select s from Seller s join s.stands st group by s order by sum(st.events.size) desc")
	Collection<Seller> dashboardQueryB4();

	@Query("select s from Report r join r.stand s group by s order by sum(r.score) desc")
	Collection<Stand> dashboardQueryA1();

	@Query("select 1.0*count(e1)/(select count(e2) from Event e2) from Event e1 where e1.sponsorships.size > 2")
	String dashboardQueryA2();

}
