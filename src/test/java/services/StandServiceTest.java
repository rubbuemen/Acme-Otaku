
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
import domain.Event;
import domain.Stand;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class StandServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private StandService	standService;

	@Autowired
	private EventService	eventService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 33.2
	 *         Caso de uso: listar "Stands"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Stands" correctamente sin estar logeado
	 *         Analisis de cobertura de sentencias: 100% 8/8 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListStand() {
		final Object testingData[][] = {
			{
				null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listStandTemplate((Class<?>) testingData[i][0]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 35.1
	 *         Caso de uso: listar "Stands" del seller logeado
	 *         Tests positivos: 1
	 *         *** 1. Listar "Stands" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Stands" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 23/23 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListStandsSeller() {
		final Object testingData[][] = {
			{
				"seller1", null
			}, {
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listStandsSellerTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 35.1
	 *         Caso de uso: crear un "Stand"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Stand" correctamente
	 *         Tests negativos: 6
	 *         *** 1. Intento de creación de un "Stand" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Stand" con tipo vacío
	 *         *** 3. Intento de creación de un "Stand" con nombre de marca vacío
	 *         *** 4. Intento de creación de un "Stand" con banner vacío
	 *         *** 5. Intento de creación de un "Stand" con banner que no es una URL
	 *         *** 6. Intento de creación de un "Stand" con un evento no disponible para asociar con un stand
	 *         Analisis de cobertura de sentencias: 86,5% 151/171 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateStand() {
		final Object testingData[][] = {
			{
				"seller1", "COMMERCIAL", "brandNameTest", "http://www.bannerTest.com", "event1", null
			}, {
				"visitor1", "COMMERCIAL", "brandNameTest", "http://www.bannerTest.com", "event1", IllegalArgumentException.class
			}, {
				"seller1", "", "brandNameTest", "http://www.bannerTest.com", "event1", ConstraintViolationException.class
			}, {
				"seller1", "COMMERCIAL", "", "http://www.bannerTest.com", "event1", ConstraintViolationException.class
			}, {
				"seller1", "COMMERCIAL", "brandNameTest", "", "event1", ConstraintViolationException.class
			}, {
				"seller1", "COMMERCIAL", "brandNameTest", "bannerTest", "event1", ConstraintViolationException.class
			}, {
				"seller1", "COMMERCIAL", "brandNameTest", "http://www.bannerTest.com", "event2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createStandTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 35.1
	 *         Caso de uso: editar un "Stand"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Stand" correctamente
	 *         Tests negativos: 8
	 *         *** 1. Intento de edición de un "Stand" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Stand" que no es del "Seller" logeado
	 *         *** 3. Intento de edición de un "Stand" con tipo vacío
	 *         *** 4. Intento de edición de un "Stand" con nombre de marca vacío
	 *         *** 5. Intento de edición de un "Stand" con banner vacío
	 *         *** 6. Intento de edición de un "Stand" con banner que no es una URL
	 *         *** 7. Intento de edición de un "Stand" con un evento no disponible para asociar con un stand
	 *         *** 8. Intento de edición de un "Stand" que ya tiene eventos asociados
	 *         Analisis de cobertura de sentencias: 86,5% 128/148 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */

	@Test
	public void driverEditStand() {
		final Object testingData[][] = {
			{
				"seller3", "stand4", "COMMERCIAL", "brandNameTest", "http://www.bannerTest.com", "event1", null
			}, {
				"visitor1", "stand4", "COMMERCIAL", "brandNameTest", "http://www.bannerTest.com", "event1", IllegalArgumentException.class
			}, {
				"seller1", "stand4", "COMMERCIAL", "brandNameTest", "http://www.bannerTest.com", "event1", IllegalArgumentException.class
			}, {
				"seller3", "stand4", "", "brandNameTest", "http://www.bannerTest.com", "event1", ConstraintViolationException.class
			}, {
				"seller3", "stand4", "COMMERCIAL", "", "http://www.bannerTest.com", "event1", ConstraintViolationException.class
			}, {
				"seller3", "stand4", "COMMERCIAL", "brandNameTest", "", "event1", ConstraintViolationException.class
			}, {
				"seller3", "stand4", "COMMERCIAL", "brandNameTest", "bannerTest", "event1", ConstraintViolationException.class
			}, {
				"seller3", "stand4", "COMMERCIAL", "brandNameTest", "http://www.bannerTest.com", "event2", IllegalArgumentException.class
			}, {
				"seller1", "stand1", "COMMERCIAL", "brandNameTest", "http://www.bannerTest.com", "event1", IllegalArgumentException.class
			},
		};

		for (int i = 0; i < testingData.length; i++)
			this.editStandTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 35.1
	 *         Caso de uso: eliminar un "Stand"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Stand" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de eliminación de un "Stand" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Stand" que no es del "Seller" logeado
	 *         *** 3. Intento de eliminación de un "Stand" que ya tiene standos asociados
	 *         Analisis de cobertura de sentencias: 98,5% 64/65 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteStand() {
		final Object testingData[][] = {
			{
				"seller3", "stand4", null
			}, {
				"visitor1", "stand4", IllegalArgumentException.class
			}, {
				"seller1", "stand4", IllegalArgumentException.class
			}, {
				"seller1", "stand1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteStandTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listStandTemplate(final Class<?> expected) {
		Class<?> caught = null;
		Collection<Stand> stands;

		super.startTransaction();

		try {
			stands = this.standService.findAll();
			Assert.notNull(stands);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.rollbackTransaction();
	}

	protected void listStandsSellerTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Stand> stands;

		super.startTransaction();

		try {
			super.authenticate(username);
			stands = this.standService.findStandsBySellerLogged();
			Assert.notNull(stands);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createStandTemplate(final String username, final String type, final String brandName, final String banner, final String event, final Class<?> expected) {
		Class<?> caught = null;
		Stand standEntity;
		final Event eventEntity = this.eventService.findOne(super.getEntityId(event));
		final Collection<Event> events = new HashSet<>();

		super.startTransaction();

		try {
			super.authenticate(username);
			events.add(eventEntity);
			standEntity = this.standService.create();
			standEntity.setType(type);
			standEntity.setBrandName(brandName);
			standEntity.setBanner(banner);
			standEntity.setEvents(events);
			this.standService.save(standEntity);
			this.standService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editStandTemplate(final String username, final String stand, final String type, final String brandName, final String banner, final String event, final Class<?> expected) {
		Class<?> caught = null;
		Stand standEntity;

		super.startTransaction();
		final Event eventEntity = this.eventService.findOne(super.getEntityId(event));
		final Collection<Event> events = new HashSet<>();

		try {
			super.authenticate(username);
			events.add(eventEntity);
			standEntity = this.standService.findStandSellerLogged(super.getEntityId(stand));
			Assert.isTrue(standEntity.getEvents().isEmpty(), "You can not edit the stand because it has some associated event");
			standEntity.setType(type);
			standEntity.setBrandName(brandName);
			standEntity.setBanner(banner);
			standEntity.setEvents(events);
			this.standService.save(standEntity);
			this.standService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteStandTemplate(final String username, final String stand, final Class<?> expected) {
		Class<?> caught = null;
		Stand standEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			standEntity = this.standService.findStandSellerLogged(super.getEntityId(stand));
			this.standService.delete(standEntity);
			this.standService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

}
