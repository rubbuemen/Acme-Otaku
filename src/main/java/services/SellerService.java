
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.SellerRepository;
import domain.Seller;

@Service
@Transactional
public class SellerService {

	// Managed repository
	@Autowired
	private SellerRepository	sellerRepository;


	// Supporting services

	// Simple CRUD methods
	public Seller create() {
		Seller result;

		result = new Seller();
		return result;
	}

	public Collection<Seller> findAll() {
		Collection<Seller> result;

		result = this.sellerRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Seller findOne(final int sellerId) {
		Assert.isTrue(sellerId != 0);

		Seller result;

		result = this.sellerRepository.findOne(sellerId);
		Assert.notNull(result);

		return result;
	}

	public Seller save(final Seller seller) {
		Assert.notNull(seller);

		Seller result;

		if (seller.getId() == 0)
			result = this.sellerRepository.save(seller);
		else
			result = this.sellerRepository.save(seller);

		return result;
	}

	public void delete(final Seller seller) {
		Assert.notNull(seller);
		Assert.isTrue(seller.getId() != 0);
		Assert.isTrue(this.sellerRepository.exists(seller.getId()));

		this.sellerRepository.delete(seller);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Seller reconstruct(final Seller seller, final BindingResult binding) {
		Seller result;

		if (seller.getId() == 0)
			result = seller;
		else {
			final Seller originalSeller = this.sellerRepository.findOne(seller.getId());
			Assert.notNull(originalSeller, "This entity does not exist");
			result = seller;
		}

		this.validator.validate(result, binding);

		this.sellerRepository.flush();

		return result;
	}

	public void flush() {
		this.sellerRepository.flush();
	}

}
