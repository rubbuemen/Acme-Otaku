
package repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer> {

	@Query("select s from Seller s join s.stands st where st.id = ?1")
	Seller findSellerByStandId(int standId);

}
