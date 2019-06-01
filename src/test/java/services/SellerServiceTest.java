
package services;

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

import utilities.AbstractTest;
import domain.Seller;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SellerServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private SellerService	sellerService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 33.1
	 *         Caso de uso: registrarse como "Seller" en el sistema
	 *         Tests positivos: 1
	 *         *** 1. Registrarse como "Seller" correctamente
	 *         Tests negativos: 8
	 *         *** 1. Intento de registro como "Seller" con el nombre vacío
	 *         *** 2. Intento de registro como "Seller" con el apellido vacío
	 *         *** 3. Intento de registro como "Seller" con el email vacío
	 *         *** 4. Intento de registro como "Seller" con el email sin cumplir el patrón adecuado
	 *         *** 5. Intento de registro como "Seller" con el usuario vacío
	 *         *** 6. Intento de registro como "Seller" con tamaño del usuario menor a 5 caracteres
	 *         *** 7. Intento de registro como "Seller" con tamaño del usuario mayor a 32 caracteres
	 *         *** 8. Intento de registro como "Seller" usuario ya usado
	 *         Analisis de cobertura de sentencias: 100% 240/240 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverRegisterAsSeller() {
		final Object testingData[][] = {
			{
				"testName", "testSurname", "testEmail@testemail.com", "testUser", "testPass", null
			}, {
				"", "testSurname", "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "", "testEmail@testemail.com", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail", "testUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "test", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "testUsertestUsertestUsertestUsertestUsertestUsertestUsertestUser", "testPass", ConstraintViolationException.class
			}, {
				"testName", "testSurname", "testEmail@testemail.com", "seller1", "testPass", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerAsSellerTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 11.2
	 *         Caso de uso: editar sus datos estando logeado
	 *         Tests positivos: 1
	 *         *** 1. Editar sus datos correctamente
	 *         Tests negativos: 5
	 *         *** 1. Intento de edición de datos de un actor que no es el logeado
	 *         *** 2. Intento de edición como "Seller" con el nombre vacío
	 *         *** 3. Intento de edición como "Seller" con el apellido vacío
	 *         *** 4. Intento de edición como "Seller" con el email vacío
	 *         *** 5. Intento de edición como "Seller" con el email sin cumplir el patrón adecuado
	 *         Analisis de cobertura de sentencias: 100% 199/199 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditData() {
		final Object testingData[][] = {
			{
				"seller1", "seller1", "testName", "testSurname", "testEmail@testemail.com", null
			}, {
				"seller1", "seller2", "testName", "testSurname", "testEmail@testemail.com", IllegalArgumentException.class
			}, {
				"seller1", "seller1", "", "testSurname", "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"seller1", "seller1", "testName", "", "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"seller1", "seller1", "testName", "testSurname", "", ConstraintViolationException.class
			}, {
				"seller1", "seller1", "testName", "testSurname", "testEmail", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editDataTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	// Template methods ------------------------------------------------------

	protected void registerAsSellerTemplate(final String name, final String surname, final String email, final String username, final String password, final Class<?> expected) {
		Class<?> caught = null;
		Seller seller;

		super.startTransaction();

		try {
			seller = this.sellerService.create();
			seller.setName(name);
			seller.setSurname(surname);
			seller.setEmail(email);
			seller.getUserAccount().setUsername(username);
			seller.getUserAccount().setPassword(password);
			this.sellerService.save(seller);
			this.sellerService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

	protected void editDataTemplate(final String username, final String actorData, final String name, final String surname, final String email, final Class<?> expected) {
		Class<?> caught = null;
		Seller seller;

		super.startTransaction();

		try {
			super.authenticate(username);
			seller = this.sellerService.findOne(super.getEntityId(actorData));
			seller.setName(name);
			seller.setSurname(surname);
			seller.setEmail(email);
			this.sellerService.save(seller);
			this.sellerService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		super.unauthenticate();
		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

}
