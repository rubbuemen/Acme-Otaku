
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
import domain.Headquarter;
import domain.Meeting;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class MeetingServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private MeetingService		meetingService;

	@Autowired
	private HeadquarterService	headquarterService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 34.1
	 *         Caso de uso: listar "Meetings"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Meetings" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de listar "Meetings" con una autoridad no permitida
	 *         *** 2. Intento de listar "Meetings" sin ser presidente de la asociación
	 *         Analisis de cobertura de sentencias: 100% 29/29 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListMeetingsMember() {
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
			this.listMeetingsMemberTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 34.1
	 *         Caso de uso: crear un "Meeting"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Meeting" correctamente
	 *         Tests negativos: 7
	 *         *** 1. Intento de creación de un "Meeting" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Meeting" con nombre vacío
	 *         *** 3. Intento de creación de un "Meeting" con descripción vacía
	 *         *** 4. Intento de creación de un "Meeting" con tipo vacía
	 *         *** 5. Intento de creación de un "Meeting" con fecha nula
	 *         *** 6. Intento de creación de un "Meeting" con fecha no futura
	 *         *** 7. Intento de creación de un "Meeting" sin ser presidente de la asociación
	 *         Analisis de cobertura de sentencias: 100% 90/90 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateMeeting() {
		final Calendar cal = Calendar.getInstance();
		cal.set(2000, 9, 30);
		final Date datePast = cal.getTime();
		cal.set(2200, 9, 30);
		final Date dateFuture = cal.getTime();
		final Object testingData[][] = {
			{
				"member1", "nameTest", "descriptionTest", "PUBLIC", dateFuture, "headquarter1", null
			}, {
				"visitor1", "nameTest", "descriptionTest", "PUBLIC", dateFuture, "headquarter1", IllegalArgumentException.class
			}, {
				"member1", "", "descriptionTest", "PUBLIC", dateFuture, "headquarter1", ConstraintViolationException.class
			}, {
				"member1", "nameTest", "", "PUBLIC", dateFuture, "headquarter1", ConstraintViolationException.class
			}, {
				"member1", "nameTest", "descriptionTest", "", dateFuture, "headquarter1", ConstraintViolationException.class
			}, {
				"member1", "nameTest", "descriptionTest", "PUBLIC", null, "headquarter1", ConstraintViolationException.class
			}, {
				"member1", "nameTest", "descriptionTest", "PUBLIC", datePast, "headquarter1", ConstraintViolationException.class
			}, {
				"member3", "nameTest", "descriptionTest", "PUBLIC", dateFuture, "headquarter1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createMeetingTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Date) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 34.1
	 *         Caso de uso: editar un "Meeting"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Meeting" correctamente
	 *         Tests negativos: 8
	 *         *** 1. Intento de edición de un "Meeting" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Meeting" que no es del "Member" logeado
	 *         *** 3. Intento de edición de un "Meeting" con nombre vacío
	 *         *** 4. Intento de edición de un "Meeting" con descripción vacía
	 *         *** 5. Intento de edición de un "Meeting" con tipo vacía
	 *         *** 6. Intento de edición de un "Meeting" con fecha nula
	 *         *** 7. Intento de edición de un "Meeting" con fecha no futura
	 *         *** 8. Intento de edición de un "Meeting" sin ser presidente de la asociación
	 *         Analisis de cobertura de sentencias: 100% 65/65 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */

	@Test
	public void driverEditMeeting() {
		final Calendar cal = Calendar.getInstance();
		cal.set(2000, 9, 30);
		final Date datePast = cal.getTime();
		cal.set(2200, 9, 30);
		final Date dateFuture = cal.getTime();
		final Object testingData[][] = {
			{
				"member1", "meeting1", "nameTest", "descriptionTest", "PUBLIC", dateFuture, "headquarter1", null
			}, {
				"visitor1", "meeting1", "nameTest", "descriptionTest", "PUBLIC", dateFuture, "headquarter1", IllegalArgumentException.class
			}, {
				"member2", "meeting1", "nameTest", "descriptionTest", "PUBLIC", dateFuture, "headquarter1", IllegalArgumentException.class
			}, {
				"member1", "meeting1", "", "descriptionTest", "PUBLIC", dateFuture, "headquarter1", ConstraintViolationException.class
			}, {
				"member1", "meeting1", "nameTest", "", "PUBLIC", dateFuture, "headquarter1", ConstraintViolationException.class
			}, {
				"member1", "meeting1", "nameTest", "descriptionTest", "", dateFuture, "headquarter1", ConstraintViolationException.class
			}, {
				"member1", "meeting1", "nameTest", "descriptionTest", "PUBLIC", null, "headquarter1", ConstraintViolationException.class
			}, {
				"member1", "meeting1", "nameTest", "descriptionTest", "PUBLIC", datePast, "headquarter1", ConstraintViolationException.class
			}, {
				"member3", "meeting3", "nameTest", "descriptionTest", "PUBLIC", dateFuture, "headquarter1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editMeetingTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Date) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 34.1
	 *         Caso de uso: eliminar un "Meeting"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Meeting" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de eliminación de un "Meeting" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Meeting" que no es del "Member" logeado
	 *         *** 3. Intento de eliminación de un "Meeting" sin ser presidente de la asociación
	 *         Analisis de cobertura de sentencias: 98,5% 65/66 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteMeeting() {
		final Object testingData[][] = {
			{
				"member1", "meeting1", null
			}, {
				"visitor1", "meeting1", IllegalArgumentException.class
			}, {
				"member2", "meeting1", IllegalArgumentException.class
			}, {
				"member3", "meeting3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteMeetingTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listMeetingsMemberTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Meeting> meetings;

		super.startTransaction();

		try {
			super.authenticate(username);
			meetings = this.meetingService.findMeetingsByMemberLogged();
			Assert.notNull(meetings);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createMeetingTemplate(final String username, final String name, final String description, final String type, final Date date, final String headquarter, final Class<?> expected) {
		Class<?> caught = null;
		Meeting meetingEntity;
		final Headquarter headquarterEntity = this.headquarterService.findOne(super.getEntityId(headquarter));

		super.startTransaction();

		try {
			super.authenticate(username);
			meetingEntity = this.meetingService.create();
			meetingEntity.setName(name);
			meetingEntity.setDescription(description);
			meetingEntity.setType(type);
			meetingEntity.setDate(date);
			meetingEntity.setHeadquarter(headquarterEntity);
			this.meetingService.save(meetingEntity);
			this.meetingService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editMeetingTemplate(final String username, final String meeting, final String name, final String description, final String type, final Date date, final String headquarter, final Class<?> expected) {
		Class<?> caught = null;
		Meeting meetingEntity;

		super.startTransaction();
		final Headquarter headquarterEntity = this.headquarterService.findOne(super.getEntityId(headquarter));

		try {
			super.authenticate(username);
			meetingEntity = this.meetingService.findMeetingMemberLogged(super.getEntityId(meeting));
			meetingEntity.setName(name);
			meetingEntity.setDescription(description);
			meetingEntity.setType(type);
			meetingEntity.setDate(date);
			meetingEntity.setHeadquarter(headquarterEntity);
			this.meetingService.save(meetingEntity);
			this.meetingService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteMeetingTemplate(final String username, final String meeting, final Class<?> expected) {
		Class<?> caught = null;
		Meeting meetingEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			meetingEntity = this.meetingService.findMeetingMemberLogged(super.getEntityId(meeting));
			this.meetingService.delete(meetingEntity);
			this.meetingService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

}
