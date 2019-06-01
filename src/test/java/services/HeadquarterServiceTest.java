
package services;

import java.util.Collection;
import java.util.HashSet;

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
import domain.Headquarter;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class HeadquarterServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private HeadquarterService	headquarterService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 34.2
	 *         Caso de uso: listar "Headquarters"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Headquarters" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de listar "Headquarters" con una autoridad no permitida
	 *         *** 2. Intento de listar "Headquarters" sin ser presidente de la asociación
	 *         Analisis de cobertura de sentencias: 100% 29/29 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListHeadquartersMember() {
		final Object testingData[][] = {
			{
				"member1", null
			}, {
				"visitor1", IllegalArgumentException.class
			}, {
				"member3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listHeadquartersMemberTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 34.2
	 *         Caso de uso: crear un "Headquarter"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Headquarter" correctamente
	 *         Tests negativos: 5
	 *         *** 1. Intento de creación de un "Headquarter" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Headquarter" con nombre vacío
	 *         *** 3. Intento de creación de un "Headquarter" con dirección vacía
	 *         *** 4. Intento de creación de un "Headquarter" con fotos vacía
	 *         *** 5. Intento de creación de un "Headquarter" sin ser presidente de la asociación
	 *         Analisis de cobertura de sentencias: 100% 90/90 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverCreateHeadquarter() {
		final Collection<String> photos = this.photos();
		final Collection<String> emptyCollection = new HashSet<>();
		final Object testingData[][] = {
			{
				"member1", "nameTest", "addressTest", photos, null
			}, {
				"visitor1", "nameTest", "addressTest", photos, IllegalArgumentException.class
			}, {
				"member1", "", "addressTest", photos, ConstraintViolationException.class
			}, {
				"member1", "nameTest", "", photos, ConstraintViolationException.class
			}, {
				"member1", "nameTest", "addressTest", emptyCollection, ConstraintViolationException.class
			}, {
				"member3", "nameTest", "addressTest", photos, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createHeadquarterTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Collection<String>) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 34.2
	 *         Caso de uso: editar un "Headquarter"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Headquarter" correctamente
	 *         Tests negativos: 6
	 *         *** 1. Intento de edición de un "Headquarter" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Headquarter" que no es del "Member" logeado
	 *         *** 3. Intento de edición de un "Headquarter" con nombre vacío
	 *         *** 4. Intento de edición de un "Headquarter" con dirección vacía
	 *         *** 5. Intento de edición de un "Headquarter" con fotos vacía
	 *         *** 6. Intento de edición de un "Headquarter" sin ser presidente de la asociación
	 *         Analisis de cobertura de sentencias: 100% 65/65 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */

	@SuppressWarnings("unchecked")
	@Test
	public void driverEditHeadquarter() {
		final Collection<String> photos = this.photos();
		final Collection<String> emptyCollection = new HashSet<>();
		final Object testingData[][] = {
			{
				"member1", "headquarter1", "nameTest", "addressTest", photos, null
			}, {
				"visitor1", "headquarter1", "nameTest", "addressTest", photos, IllegalArgumentException.class
			}, {
				"member2", "headquarter1", "nameTest", "addressTest", photos, IllegalArgumentException.class
			}, {
				"member1", "headquarter1", "", "addressTest", photos, ConstraintViolationException.class
			}, {
				"member1", "headquarter1", "nameTest", "", photos, ConstraintViolationException.class
			}, {
				"member1", "headquarter1", "nameTest", "addressTest", emptyCollection, ConstraintViolationException.class
			}, {
				"member3", "headquarter1", "nameTest", "addressTest", photos, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editHeadquarterTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Collection<String>) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	// Template methods ------------------------------------------------------

	protected void listHeadquartersMemberTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Headquarter> headquarters;

		super.startTransaction();

		try {
			super.authenticate(username);
			headquarters = this.headquarterService.findHeadquartersByMemberLogged();
			Assert.notNull(headquarters);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createHeadquarterTemplate(final String username, final String name, final String address, final Collection<String> photos, final Class<?> expected) {
		Class<?> caught = null;
		final Headquarter headquarterEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			headquarterEntity = this.headquarterService.create();
			headquarterEntity.setName(name);
			headquarterEntity.setAddress(address);
			headquarterEntity.setPhotos(photos);
			this.headquarterService.save(headquarterEntity);
			this.headquarterService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editHeadquarterTemplate(final String username, final String headquarter, final String name, final String address, final Collection<String> photos, final Class<?> expected) {
		Class<?> caught = null;
		final Headquarter headquarterEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			headquarterEntity = this.headquarterService.findHeadquarterMemberLogged(super.getEntityId(headquarter));
			headquarterEntity.setName(name);
			headquarterEntity.setAddress(address);
			headquarterEntity.setPhotos(photos);
			this.headquarterService.save(headquarterEntity);
			this.headquarterService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	// Auxiliar methods ------------------------------------------------------

	private Collection<String> photos() {
		final Collection<String> photos = new HashSet<>();
		final String photo1 = "http://www.phototest1.com";
		final String photo2 = "http://www.phototest2.com";
		photos.add(photo1);
		photos.add(photo2);

		return photos;
	}

}
