
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.ProductRepository;
import domain.Actor;
import domain.Product;
import domain.Seller;
import domain.Stand;

@Service
@Transactional
public class ProductService {

	// Managed repository
	@Autowired
	private ProductRepository	productRepository;

	// Supporting services
	@Autowired
	private ActorService		actorService;

	@Autowired
	private StandService		standService;

	@Autowired
	private SellerService		sellerService;


	// Simple CRUD methods
	//R35.2
	public Product create() {
		Product result;

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSeller(actorLogged);

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

	//R35.2
	public Product save(final Product product, final Stand stand) {
		Assert.notNull(product);

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSeller(actorLogged);

		Product result;

		final Seller sellerOwner = this.sellerService.findSellerByStandId(stand.getId());
		Assert.isTrue(actorLogged.equals(sellerOwner), "The logged actor is not the owner of this entity");

		if (product.getId() == 0) {
			final Collection<Product> productsStand = stand.getProducts();
			result = this.productRepository.save(product);
			productsStand.add(result);
			stand.setProducts(productsStand);
			this.standService.saveAuxiliar(stand);
		} else
			result = this.productRepository.save(product);

		return result;
	}

	//R35.2
	public void delete(final Product product, final Stand stand) {
		Assert.notNull(product);
		Assert.isTrue(product.getId() != 0);
		Assert.isTrue(this.productRepository.exists(product.getId()));

		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSeller(actorLogged);

		final Seller sellerOwner = this.sellerService.findSellerByStandId(stand.getId());
		Assert.isTrue(actorLogged.equals(sellerOwner), "The logged actor is not the owner of this entity");

		final Collection<Product> productsStand = stand.getProducts();
		productsStand.remove(product);
		stand.setProducts(productsStand);
		this.standService.saveAuxiliar(stand);

		this.productRepository.delete(product);
	}

	// Other business methods
	public Collection<Product> findProductsByStandId(final int standId) {
		Collection<Product> result;

		result = this.productRepository.findProductsByStandId(standId);
		Assert.notNull(result);

		return result;
	}

	//R35.2
	public Collection<Product> findProductsByStandSellerLogged(final int standId) {
		final Actor actorLogged = this.actorService.findActorLogged();
		Assert.notNull(actorLogged);
		this.actorService.checkUserLoginSeller(actorLogged);

		final Collection<Product> result;

		final Stand stand = this.standService.findStandSellerLogged(standId);

		result = stand.getProducts();
		Assert.notNull(result);

		return result;
	}


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
