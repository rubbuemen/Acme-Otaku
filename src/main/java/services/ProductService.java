
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProductRepository;
import domain.Product;

@Service
@Transactional
public class ProductService {

	// Managed repository
	@Autowired
	private ProductRepository	productRepository;


	// Supporting services

	// Simple CRUD methods
	public Product create() {
		Product result;

		result = new Product();
		return result;
	}

	public Collection<Product> findAll() {
		Collection<Product> result;

		result = this.productRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Product findOne(final int productId) {
		Assert.isTrue(productId != 0);

		Product result;

		result = this.productRepository.findOne(productId);
		Assert.notNull(result);

		return result;
	}

	public Product save(final Product product) {
		Assert.notNull(product);

		Product result;

		if (product.getId() == 0)
			result = this.productRepository.save(product);
		else
			result = this.productRepository.save(product);

		return result;
	}

	public void delete(final Product product) {
		Assert.notNull(product);
		Assert.isTrue(product.getId() != 0);
		Assert.isTrue(this.productRepository.exists(product.getId()));

		this.productRepository.delete(product);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Product reconstruct(final Product product, final BindingResult binding) {
		Product result;

		if (product.getId() == 0)
			result = product;
		else {
			final Product originalProduct = this.productRepository.findOne(product.getId());
			Assert.notNull(originalProduct, "This entity does not exist");
			result = product;
		}

		this.validator.validate(result, binding);

		this.productRepository.flush();

		return result;
	}

	public void flush() {
		this.productRepository.flush();
	}

}
