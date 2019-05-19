
package services;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import repositories.CategoryRepository;
import domain.Category;

@Service
@Transactional
public class CategoryService {

	// Managed repository
	@Autowired
	private CategoryRepository	categoryRepository;


	// Supporting services

	// Simple CRUD methods
	public Category create() {
		Category result;

		result = new Category();
		return result;
	}

	public Collection<Category> findAll() {
		Collection<Category> result;

		result = this.categoryRepository.findAll();
		Assert.notNull(result);

		return result;
	}

	public Category findOne(final int categoryId) {
		Assert.isTrue(categoryId != 0);

		Category result;

		result = this.categoryRepository.findOne(categoryId);
		Assert.notNull(result);

		return result;
	}

	public Category save(final Category category) {
		Assert.notNull(category);

		Category result;

		if (category.getId() == 0)
			result = this.categoryRepository.save(category);
		else
			result = this.categoryRepository.save(category);

		return result;
	}

	public void delete(final Category category) {
		Assert.notNull(category);
		Assert.isTrue(category.getId() != 0);
		Assert.isTrue(this.categoryRepository.exists(category.getId()));

		this.categoryRepository.delete(category);
	}


	// Other business methods

	// Reconstruct methods
	@Autowired
	private Validator	validator;


	public Category reconstruct(final Category category, final BindingResult binding) {
		Category result;

		if (category.getId() == 0)
			result = category;
		else {
			final Category originalCategory = this.categoryRepository.findOne(category.getId());
			Assert.notNull(originalCategory, "This entity does not exist");
			result = category;
		}

		this.validator.validate(result, binding);

		this.categoryRepository.flush();

		return result;
	}

	public void flush() {
		this.categoryRepository.flush();
	}

}
