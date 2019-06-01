
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintViolationException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Product;
import domain.Stand;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ProductServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private ProductService	productService;

	@Autowired
	private StandService	standService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 35.2
	 *         Caso de uso: listar "Products" de un "Stand"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Products" de un "Stand" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de listar "Products" con una autoridad no permitida
	 *         *** 2. Intento de listar "Products" que no pertenecen al "Seller" logeado
	 *         Analisis de cobertura de sentencias: 100% 22/22 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListProducts() {
		final Object testingData[][] = {
			{
				"seller1", "stand1", null
			}, {
				"visitor1", "stand1", IllegalArgumentException.class
			}, {
				"seller1", "stand3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listProductsTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 35.2
	 *         Caso de uso: crear un "Product" en un "Stand"
	 *         Tests positivos: 2
	 *         *** 1. Crear un "Product" en un "Stand" correctamente
	 *         Tests negativos: 8
	 *         *** 1. Intento de creación de un "Product" en un "Stand" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Product" en un "Stand" que no es del "Seller" logeado
	 *         *** 3. Intento de creación de un "Product" cuyo nombre es vacío
	 *         *** 4. Intento de creación de un "Product" cuya descripción es vacía
	 *         *** 5. Intento de creación de un "Product" cuyo precio es nulo
	 *         *** 6. Intento de creación de un "Product" cuyo precio es menor que 0
	 *         *** 7. Intento de creación de un "Product" cuya foto es vacía
	 *         *** 8. Intento de creación de un "Product" cuya foto no es una URL
	 *         Analisis de cobertura de sentencias: 100% 72/72 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateProduct() {
		final Object testingData[][] = {
			{
				"seller1", "stand1", "nameTest", "descriptionTest", 20.0, "http://www.photoTest.com", null
			}, {
				"visitor1", "stand1", "nameTest", "descriptionTest", 20.0, "http://www.photoTest.com", IllegalArgumentException.class
			}, {
				"seller1", "stand3", "nameTest", "descriptionTest", 20.0, "http://www.photoTest.com", IllegalArgumentException.class
			}, {
				"seller1", "stand1", "", "descriptionTest", 20.0, "http://www.photoTest.com", ConstraintViolationException.class
			}, {
				"seller1", "stand1", "nameTest", "", 20.0, "http://www.photoTest.com", ConstraintViolationException.class
			}, {
				"seller1", "stand1", "nameTest", "descriptionTest", null, "http://www.photoTest.com", ConstraintViolationException.class
			}, {
				"seller1", "stand1", "nameTest", "descriptionTest", -1.0, "http://www.photoTest.com", ConstraintViolationException.class
			}, {
				"seller1", "stand1", "nameTest", "descriptionTest", 20.0, "", ConstraintViolationException.class
			}, {
				"seller1", "stand1", "nameTest", "descriptionTest", 20.0, "photoTest", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createProductTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Double) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 35.2
	 *         Caso de uso: editar un "Product" en un "Stand"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Product" de un "Stand" correctamente
	 *         Tests negativos: 8
	 *         *** 1. Intento de edición de un "Product" en un "Stand" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Product" en un "Stand" que no es del "Seller" logeado
	 *         *** 3. Intento de edición de un "Product" cuyo nombre es vacío
	 *         *** 4. Intento de edición de un "Product" cuya descripción es vacía
	 *         *** 5. Intento de edición de un "Product" cuyo precio es nulo
	 *         *** 6. Intento de edición de un "Product" cuyo precio es menor que 0
	 *         *** 7. Intento de edición de un "Product" cuya foto es vacía
	 *         *** 8. Intento de edición de un "Product" cuya foto no es una URL
	 *         Analisis de cobertura de sentencias: 100% 56/56 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditProduct() {
		final Object testingData[][] = {
			{
				"seller1", "stand1", "product1", "nameTest", "descriptionTest", 20.0, "http://www.photoTest.com", null
			}, {
				"visitor1", "stand1", "product1", "nameTest", "descriptionTest", 20.0, "http://www.photoTest.com", IllegalArgumentException.class
			}, {
				"seller1", "stand3", "product5", "nameTest", "descriptionTest", 20.0, "http://www.photoTest.com", IllegalArgumentException.class
			}, {
				"seller1", "stand1", "product1", "", "descriptionTest", 20.0, "http://www.photoTest.com", ConstraintViolationException.class
			}, {
				"seller1", "stand1", "product1", "nameTest", "", 20.0, "http://www.photoTest.com", ConstraintViolationException.class
			}, {
				"seller1", "stand1", "product1", "nameTest", "descriptionTest", null, "http://www.photoTest.com", ConstraintViolationException.class
			}, {
				"seller1", "stand1", "product1", "nameTest", "descriptionTest", -1.0, "http://www.photoTest.com", ConstraintViolationException.class
			}, {
				"seller1", "stand1", "product1", "nameTest", "descriptionTest", 20.0, "", ConstraintViolationException.class
			}, {
				"seller1", "stand1", "product1", "nameTest", "descriptionTest", 20.0, "photoTest", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editProductTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Double) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 35.2
	 *         Caso de uso: eliminar un "Product"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Product" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de eliminación de un "Product" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Product" en un "Stand" que no es del "Seller" logeado
	 *         Analisis de cobertura de sentencias: 98.2% 56/57 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteProduct() {
		final Object testingData[][] = {
			{
				"seller1", "stand1", "product1", null
			}, {
				"visitor1", "stand1", "product1", IllegalArgumentException.class
			}, {
				"seller1", "stand3", "product5", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteProductTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	// Template methods ------------------------------------------------------

	protected void listProductsTemplate(final String username, final String stand, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Product> products;

		super.startTransaction();

		try {
			super.authenticate(username);
			products = this.productService.findProductsByStandSellerLogged(super.getEntityId(stand));
			Assert.notNull(products);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createProductTemplate(final String username, final String stand, final String name, final String description, final Double price, final String photo, final Class<?> expected) {
		Class<?> caught = null;
		Product product;
		Stand standEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			standEntity = this.standService.findOne(super.getEntityId(stand));
			product = this.productService.create();
			product.setName(name);
			product.setDescription(description);
			product.setPrice(price);
			product.setPhoto(photo);
			product = this.productService.save(product, standEntity);
			this.productService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editProductTemplate(final String username, final String stand, final String product, final String name, final String description, final Double price, final String photo, final Class<?> expected) {
		Class<?> caught = null;
		Product productEntity;
		Stand standEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			standEntity = this.standService.findOne(super.getEntityId(stand));
			productEntity = this.productService.findOne(super.getEntityId(product));
			productEntity.setName(name);
			productEntity.setDescription(description);
			productEntity.setPrice(price);
			productEntity.setPhoto(photo);
			this.productService.save(productEntity, standEntity);
			this.productService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteProductTemplate(final String username, final String stand, final String product, final Class<?> expected) {
		Class<?> caught = null;
		Product productEntity;
		Stand standEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			standEntity = this.standService.findOne(super.getEntityId(stand));
			this.productService.findProductsByStandSellerLogged(super.getEntityId(stand));
			productEntity = this.productService.findOne(super.getEntityId(product));
			this.productService.delete(productEntity, standEntity);
			this.productService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
