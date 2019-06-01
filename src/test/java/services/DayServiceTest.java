
package services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

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
import domain.Day;
import domain.Event;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DayServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private DayService		dayService;

	@Autowired
	private EventService	eventService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.2
	 *         Caso de uso: listar "Days" de un "Event"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Days" de un "Event" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de listar "Days" con una autoridad no permitida
	 *         *** 2. Intento de listar "Days" que no pertenecen al "Member" logeado
	 *         Analisis de cobertura de sentencias: 100% 22/22 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListDays() {
		final Object testingData[][] = {
			{
				"member1", "event2", null
			}, {
				"visitor1", "event2", IllegalArgumentException.class
			}, {
				"member1", "event4", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listDaysTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.2
	 *         Caso de uso: crear un "Day" en un "Event"
	 *         Tests positivos: 2
	 *         *** 1. Crear un "Day" en un "Event" correctamente
	 *         Tests negativos: 5
	 *         *** 1. Intento de creación de un "Day" en un "Event" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Day" en un "Event" que no es del "Member" logeado
	 *         *** 3. Intento de creación de un "Day" cuyo precio es nulo
	 *         *** 4. Intento de creación de un "Day" cuyo precio es menor que 0
	 *         *** 5. Intento de creación de un "Day" cuyo evento está en modo final
	 *         Analisis de cobertura de sentencias: 62,1% 227/356 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateDay() {
		final Object testingData[][] = {
			{
				"member1", "event2", 20.0, null
			}, {
				"visitor1", "event2", 20.0, IllegalArgumentException.class
			}, {
				"member2", "event2", 20.0, IllegalArgumentException.class
			}, {
				"member1", "event2", null, ConstraintViolationException.class
			}, {
				"member1", "event2", -1.0, ConstraintViolationException.class
			}, {
				"member1", "event1", 20.0, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createDayTemplate((String) testingData[i][0], (String) testingData[i][1], (Double) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.2
	 *         Caso de uso: editar un "Day" en un "Event"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Day" de un "Event" correctamente
	 *         Tests negativos: 7
	 *         *** 1. Intento de edición de un "Day" en un "Event" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Day" en un "Event" que no es del "Member" logeado
	 *         *** 3. Intento de edición de un "Day" cuya fecha es nula
	 *         *** 4. Intento de edición de un "Day" cuya fecha no es futura
	 *         *** 5. Intento de edición de un "Day" cuyo precio es nulo
	 *         *** 6. Intento de edición de un "Day" cuyo precio es menor que 0
	 *         *** 7. Intento de edición de un "Day" cuyo evento está en modo final
	 *         Analisis de cobertura de sentencias: 62,1% 211/340 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditDay() {
		final Calendar cal = Calendar.getInstance();
		cal.set(2000, 9, 30);
		final Date datePast = cal.getTime();
		cal.set(2200, 9, 30);
		final Date dateFuture = cal.getTime();

		final Object testingData[][] = {
			{
				"member1", "event2", "day4", dateFuture, 20.0, null
			}, {
				"visitor1", "event2", "day4", dateFuture, 20.0, IllegalArgumentException.class
			}, {
				"member2", "event2", "day4", dateFuture, 20.0, IllegalArgumentException.class
			}, {
				"member1", "event2", "day4", null, 20.0, ConstraintViolationException.class
			}, {
				"member1", "event2", "day4", datePast, 20.0, IllegalArgumentException.class
			}, {
				"member1", "event2", "day4", dateFuture, null, ConstraintViolationException.class
			}, {
				"member1", "event2", "day4", dateFuture, -1.0, ConstraintViolationException.class
			}, {
				"member1", "event1", "day1", dateFuture, 20.0, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editDayTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (Double) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.2
	 *         Caso de uso: eliminar un "Day"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Day" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de eliminación de un "Day" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Day" en un "Event" que no es del "Member" logeado
	 *         *** 3. Intento de eliminación de un "Day" en un "Event" que está en modo final
	 *         Analisis de cobertura de sentencias: 56,6% 73/129 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteDay() {
		final Object testingData[][] = {
			{
				"member1", "event2", "day3", null
			}, {
				"visitor1", "event2", "day3", IllegalArgumentException.class
			}, {
				"member2", "event2", "day3", IllegalArgumentException.class
			}, {
				"member2", "event1", "day2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteDayTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Class<?>) testingData[i][3]);
	}

	// Template methods ------------------------------------------------------

	protected void listDaysTemplate(final String username, final String event, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Day> days;

		super.startTransaction();

		try {
			super.authenticate(username);
			days = this.dayService.findDaysByEventMemberLogged(super.getEntityId(event));
			Assert.notNull(days);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createDayTemplate(final String username, final String event, final Double price, final Class<?> expected) {
		Class<?> caught = null;
		Day day;
		Event eventEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			eventEntity = this.eventService.findOne(super.getEntityId(event));
			day = this.dayService.create();
			day.setPrice(price);
			day = this.dayService.save(day, eventEntity);
			this.dayService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editDayTemplate(final String username, final String event, final String day, final Date date, final Double price, final Class<?> expected) {
		Class<?> caught = null;
		Day dayEntity;
		Event eventEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			eventEntity = this.eventService.findOne(super.getEntityId(event));
			dayEntity = this.dayService.findOne(super.getEntityId(day));
			dayEntity.setDate(date);
			dayEntity.setPrice(price);
			this.dayService.save(dayEntity, eventEntity);
			this.dayService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteDayTemplate(final String username, final String event, final String day, final Class<?> expected) {
		Class<?> caught = null;
		Day dayEntity;
		Event eventEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			eventEntity = this.eventService.findOne(super.getEntityId(event));
			this.dayService.findDaysByEventMemberLogged(super.getEntityId(event));
			dayEntity = this.dayService.findOne(super.getEntityId(day));
			this.dayService.delete(dayEntity, eventEntity);
			this.dayService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
