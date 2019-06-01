
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
import domain.Visitor;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class VisitorServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private VisitorService	visitorService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 10.1
	 *         Caso de uso: registrarse como "Visitor" en el sistema
	 *         Tests positivos: 1
	 *         *** 1. Registrarse como "Visitor" correctamente
	 *         Tests negativos: 8
	 *         *** 1. Intento de registro como "Visitor" con el nombre vacío
	 *         *** 2. Intento de registro como "Visitor" con el apellido vacío
	 *         *** 3. Intento de registro como "Visitor" con el email vacío
	 *         *** 4. Intento de registro como "Visitor" con el email sin cumplir el patrón adecuado
	 *         *** 5. Intento de registro como "Visitor" con el usuario vacío
	 *         *** 6. Intento de registro como "Visitor" con tamaño del usuario menor a 5 caracteres
	 *         *** 7. Intento de registro como "Visitor" con tamaño del usuario mayor a 32 caracteres
	 *         *** 8. Intento de registro como "Visitor" usuario ya usado
	 *         Analisis de cobertura de sentencias: 100% 247/247 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverRegisterAsVisitor() {
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
				"testName", "testSurname", "testEmail@testemail.com", "visitor1", "testPass", DataIntegrityViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.registerAsVisitorTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 11.2
	 *         Caso de uso: editar sus datos estando logeado
	 *         Tests positivos: 1
	 *         *** 1. Editar sus datos correctamente
	 *         Tests negativos: 5
	 *         *** 1. Intento de edición de datos de un actor que no es el logeado
	 *         *** 2. Intento de edición como "Visitor" con el nombre vacío
	 *         *** 3. Intento de edición como "Visitor" con el apellido vacío
	 *         *** 4. Intento de edición como "Visitor" con el email vacío
	 *         *** 5. Intento de edición como "Visitor" con el email sin cumplir el patrón adecuado
	 *         Analisis de cobertura de sentencias: 100% 199/199 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditData() {
		final Object testingData[][] = {
			{
				"visitor1", "visitor1", "testName", "testSurname", "testEmail@testemail.com", null
			}, {
				"visitor1", "visitor2", "testName", "testSurname", "testEmail@testemail.com", IllegalArgumentException.class
			}, {
				"visitor1", "visitor1", "", "testSurname", "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"visitor1", "visitor1", "testName", "", "testEmail@testemail.com", ConstraintViolationException.class
			}, {
				"visitor1", "visitor1", "testName", "testSurname", "", ConstraintViolationException.class
			}, {
				"visitor1", "visitor1", "testName", "testSurname", "testEmail", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editDataTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	// Template methods ------------------------------------------------------

	protected void registerAsVisitorTemplate(final String name, final String surname, final String email, final String username, final String password, final Class<?> expected) {
		Class<?> caught = null;
		Visitor visitor;

		super.startTransaction();

		try {
			visitor = this.visitorService.create();
			visitor.setName(name);
			visitor.setSurname(surname);
			visitor.setEmail(email);
			visitor.getUserAccount().setUsername(username);
			visitor.getUserAccount().setPassword(password);
			this.visitorService.save(visitor);
			this.visitorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

	protected void editDataTemplate(final String username, final String actorData, final String name, final String surname, final String email, final Class<?> expected) {
		Class<?> caught = null;
		Visitor visitor;

		super.startTransaction();

		try {
			super.authenticate(username);
			visitor = this.visitorService.findOne(super.getEntityId(actorData));
			visitor.setName(name);
			visitor.setSurname(surname);
			visitor.setEmail(email);
			this.visitorService.save(visitor);
			this.visitorService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		super.unauthenticate();
		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

}
