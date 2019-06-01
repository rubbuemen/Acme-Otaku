
package services;

import java.util.Calendar;
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
import domain.Activity;
import domain.Day;
import domain.Event;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EventServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private EventService	eventService;

	@Autowired
	private DayService		dayService;

	@Autowired
	private ActivityService	activityService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.1
	 *         Caso de uso: listar "Events"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Events" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de listar "Events" con una autoridad no permitida
	 *         *** 2. Intento de listar "Events" sin pertenecer a una asociación
	 *         Analisis de cobertura de sentencias: 100% 27/27 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListEventsMember() {
		final Object testingData[][] = {
			{
				"member1", null
			}, {
				"visitor1", IllegalArgumentException.class
			}, {
				"member6", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listEventsMemberTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.1
	 *         Caso de uso: crear un "Event"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Event" correctamente
	 *         Tests negativos: 6
	 *         *** 1. Intento de creación de un "Event" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Event" con nombre vacío
	 *         *** 3. Intento de creación de un "Event" con descripción vacía
	 *         *** 4. Intento de creación de un "Event" con dirección vacía
	 *         *** 5. Intento de creación de un "Event" sin pertenecer a una asociación
	 *         *** 6. Intento de creación de un "Event" con una actividad que no pertenece a tu asociación
	 *         Analisis de cobertura de sentencias: 100% 192/192 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateEvent() {
		final Object testingData[][] = {
			{
				"member1", "nameTest", "descriptionTest", "addressTest", "activity2", null
			}, {
				"visitor1", "nameTest", "descriptionTest", "addressTest", "activity2", IllegalArgumentException.class
			}, {
				"member1", "", "descriptionTest", "addressTest", "activity2", ConstraintViolationException.class
			}, {
				"member1", "nameTest", "", "addressTest", "activity2", ConstraintViolationException.class
			}, {
				"member1", "nameTest", "descriptionTest", "", "activity2", ConstraintViolationException.class
			}, {
				"member6", "nameTest", "descriptionTest", "addressTest", "activity2", IllegalArgumentException.class
			}, {
				"member1", "nameTest", "descriptionTest", "addressTest", "activity10", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createEventTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.1
	 *         Caso de uso: editar un "Event"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Event" correctamente
	 *         Tests negativos: 8
	 *         *** 1. Intento de edición de un "Event" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Event" que no es del "Member" logeado
	 *         *** 3. Intento de edición de un "Event" con nombre vacío
	 *         *** 4. Intento de edición de un "Event" con descripción vacía
	 *         *** 5. Intento de edición de un "Event" con dirección vacía
	 *         *** 6. Intento de edición de un "Event" sin pertenecer a una asociación
	 *         *** 7. Intento de edición de un "Event" con una actividad que no pertenece a tu asociación
	 *         *** 8. Intento de edición de un "Event" que está en modo final
	 *         Analisis de cobertura de sentencias: 100% 118/118 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */

	@Test
	public void driverEditEvent() {
		final Object testingData[][] = {
			{
				"member1", "event2", "nameTest", "descriptionTest", "addressTest", "activity2", null
			}, {
				"visitor1", "event2", "nameTest", "descriptionTest", "addressTest", "activity2", IllegalArgumentException.class
			}, {
				"member3", "event4", "nameTest", "descriptionTest", "addressTest", "activity2", IllegalArgumentException.class
			}, {
				"member1", "event2", "", "descriptionTest", "addressTest", "activity2", ConstraintViolationException.class
			}, {
				"member1", "event2", "nameTest", "", "addressTest", "activity2", ConstraintViolationException.class
			}, {
				"member1", "event2", "nameTest", "descriptionTest", "", "activity2", ConstraintViolationException.class
			}, {
				"member6", "event2", "nameTest", "descriptionTest", "addressTest", "activity2", IllegalArgumentException.class
			}, {
				"member1", "event2", "nameTest", "descriptionTest", "addressTest", "activity10", IllegalArgumentException.class
			}, {
				"member1", "event1", "nameTest", "descriptionTest", "addressTest", "activity10", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editEventTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.1
	 *         Caso de uso: eliminar un "Event"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Event" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de eliminación de un "Event" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Event" que no es del "Member" logeado
	 *         *** 3. Intento de eliminación de un "Event" que está en modo final
	 *         Analisis de cobertura de sentencias: 98,6% 68/69 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteEvent() {
		final Object testingData[][] = {
			{
				"member1", "event2", null
			}, {
				"visitor1", "event2", IllegalArgumentException.class
			}, {
				"member4", "event2", IllegalArgumentException.class
			}, {
				"member1", "event1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteEventTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.1
	 *         Caso de uso: cambiar a modo final un "Event"
	 *         Tests positivos: 1
	 *         *** 1. Cambiar a modo final un "Event" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de cambiar a modo final un "Event" con una autoridad no permitida
	 *         *** 2. Intento de cambiar a modo final un "Event" que no es del "Member" logeado
	 *         *** 3. Intento de cambiar a modo final un "Event" que ya está en modo final
	 *         Analisis de cobertura de sentencias: 98,3% 57/58 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverChangeFinalModeEvent() {
		final Object testingData[][] = {
			{
				"member1", "event2", null
			}, {
				"visitor1", "event2", IllegalArgumentException.class
			}, {
				"member4", "event2", IllegalArgumentException.class
			}, {
				"member1", "event1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.changeFinalModeEventTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 15.1
	 *         Caso de uso: listar "Events" disponibles como visitante
	 *         Tests positivos: 1
	 *         *** 1. Listar "Events" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Events" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 18/18 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListEventsVisitor() {
		final Object testingData[][] = {
			{
				"visitor1", null
			}, {
				"seller2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listEventsVisitorTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 15.2
	 *         Caso de uso: listar "Events" disponibles usando filtros como visitante
	 *         Tests positivos: 1
	 *         *** 1. Listar "Events" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Events" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 19/19 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListEventsFilterVisitor() {
		final Object testingData[][] = {
			{
				"visitor1", null
			}, {
				"seller2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listEventsFilterVisitorTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 35.3
	 *         Caso de uso: listar "Events" como seller logeado
	 *         Tests positivos: 1
	 *         *** 1. Listar "Events" donde el seller logeado tiene stands registrados
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Events" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 20/20 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListEventsSeller() {
		final Object testingData[][] = {
			{
				"seller1", null
			}, {
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listEventsSellerTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	// Template methods ------------------------------------------------------

	protected void listEventsMemberTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Event> events;

		super.startTransaction();

		try {
			super.authenticate(username);
			events = this.eventService.findEventsByMemberLogged();
			Assert.notNull(events);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createEventTemplate(final String username, final String name, final String description, final String address, final String activity, final Class<?> expected) {
		Class<?> caught = null;
		Event eventEntity;
		Day dayEntity;
		final Activity activityEntity = this.activityService.findOne(super.getEntityId(activity));
		final Collection<Day> days = new HashSet<>();
		final Collection<Activity> activities = new HashSet<>();

		super.startTransaction();

		try {
			super.authenticate(username);
			final Calendar cal = Calendar.getInstance();
			cal.set(2020, 9, 30);
			dayEntity = this.dayService.create();
			dayEntity.setDate(cal.getTime());
			dayEntity.setPrice(20.0);
			dayEntity = this.dayService.saveAuxiliar(dayEntity);
			days.add(dayEntity);
			activities.add(activityEntity);
			eventEntity = this.eventService.create();
			eventEntity.setName(name);
			eventEntity.setDescription(description);
			eventEntity.setAddress(address);
			eventEntity.setDays(days);
			eventEntity.setActivities(activities);
			this.eventService.save(eventEntity);
			this.eventService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editEventTemplate(final String username, final String event, final String name, final String description, final String address, final String activity, final Class<?> expected) {
		Class<?> caught = null;
		Event eventEntity;

		super.startTransaction();
		final Activity activityEntity = this.activityService.findOne(super.getEntityId(activity));
		final Collection<Activity> activities = new HashSet<>();

		try {
			super.authenticate(username);
			activities.add(activityEntity);
			eventEntity = this.eventService.findEventMemberLogged(super.getEntityId(event));
			eventEntity.setName(name);
			eventEntity.setDescription(description);
			eventEntity.setAddress(address);
			eventEntity.setActivities(activities);
			this.eventService.save(eventEntity);
			this.eventService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteEventTemplate(final String username, final String event, final Class<?> expected) {
		Class<?> caught = null;
		Event eventEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			eventEntity = this.eventService.findEventMemberLogged(super.getEntityId(event));
			this.eventService.delete(eventEntity);
			this.eventService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void changeFinalModeEventTemplate(final String username, final String event, final Class<?> expected) {
		Class<?> caught = null;
		Event eventEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			eventEntity = this.eventService.findEventMemberLogged(super.getEntityId(event));
			this.eventService.changeFinalMode(eventEntity);
			this.eventService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void listEventsVisitorTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Event> events;

		super.startTransaction();

		try {
			super.authenticate(username);
			events = this.eventService.findEventsFinalModeNotFinishedVisitor();
			Assert.notNull(events);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void listEventsFilterVisitorTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Event> events;

		super.startTransaction();

		try {
			super.authenticate(username);
			events = this.eventService.findEventsFinalModeNotFinishedBySingleKeyWord("Onyxia");
			Assert.notNull(events);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void listEventsSellerTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Event> events;

		super.startTransaction();

		try {
			super.authenticate(username);
			events = this.eventService.findEventsBySellerLoggedStandsRegistered();
			Assert.notNull(events);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

}
