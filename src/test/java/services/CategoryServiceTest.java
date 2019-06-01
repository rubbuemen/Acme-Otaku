
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Category;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class CategoryServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private CategoryService	categoryService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 16.2
	 *         Caso de uso: listar "Categories"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Categories" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Categories" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 24/24 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListCategory() {
		final Object testingData[][] = {
			{
				"admin", null
			}, {
				"member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listCategoryTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 16.2
	 *         Caso de uso: crear un "Category"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Category" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de creación de un "Category" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Category" con nombre en inglés vacío
	 *         *** 3. Intento de creación de un "Category" con nombre en español vacío
	 *         *** 4. Intento de creación de un "Category" con nombres ya creados
	 *         Analisis de cobertura de sentencias: 100% 36/36 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateCategory() {
		final Object testingData[][] = {
			{
				"admin", "nameENTest", "nameESTest", null
			}, {
				"member1", "nameENTest", "nameESTest", IllegalArgumentException.class
			}, {
				"admin", "", "nameESTest", ConstraintViolationException.class
			}, {
				"admin", "nameENTest", "", ConstraintViolationException.class
			}, {
				"admin", "Contest", "Concurso", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createCategoryTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 16.2
	 *         Caso de uso: editar un "Category"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Category" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de edición de un "Category" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Category" con nombre en inglés vacío
	 *         *** 3. Intento de edición de un "Category" con nombre en español vacío
	 *         *** 4. Intento de edición de un "Category" con nombres ya creados
	 *         Analisis de cobertura de sentencias: 100% 20/20 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditCategory() {
		final Object testingData[][] = {
			{
				"admin", "category1", "nameENTest", "nameESTest", null
			}, {
				"member1", "category1", "nameENTest", "nameESTest", IllegalArgumentException.class
			}, {
				"admin", "category1", "", "nameESTest", ConstraintViolationException.class
			}, {
				"admin", "category1", "nameENTest", "", ConstraintViolationException.class
			}, {
				"admin", "category2", "Contest", "Concurso", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editCategoryTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 16.2
	 *         Caso de uso: eliminar un "Category"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Category" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de eliminación de un "Category" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Category" que ya está en uso
	 *         Analisis de cobertura de sentencias: 97,7% 43/44 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteCategory() {
		final Object testingData[][] = {
			{
				"admin", "category4", null
			}, {
				"member1", "category4", IllegalArgumentException.class
			}, {
				"admin", "category1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteCategoryTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listCategoryTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Category> categories;

		super.startTransaction();

		try {
			super.authenticate(username);
			categories = this.categoryService.findAll();
			this.categoryService.findCategoriesUsed();
			Assert.notNull(categories);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createCategoryTemplate(final String username, final String nameEnglish, final String nameSpanish, final Class<?> expected) {
		Class<?> caught = null;
		Category categoryEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			categoryEntity = this.categoryService.create();
			categoryEntity.setNameEnglish(nameEnglish);
			categoryEntity.setNameSpanish(nameSpanish);
			categoryEntity = this.categoryService.save(categoryEntity);
			this.categoryService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editCategoryTemplate(final String username, final String category, final String nameEnglish, final String nameSpanish, final Class<?> expected) {
		Class<?> caught = null;
		Category categoryEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			categoryEntity = this.categoryService.findOne(super.getEntityId(category));
			categoryEntity.setNameEnglish(nameEnglish);
			categoryEntity.setNameSpanish(nameSpanish);
			categoryEntity = this.categoryService.save(categoryEntity);
			this.categoryService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteCategoryTemplate(final String username, final String category, final Class<?> expected) {
		Class<?> caught = null;
		Category categoryEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			categoryEntity = this.categoryService.findOne(super.getEntityId(category));
			this.categoryService.delete(categoryEntity);
			this.categoryService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
