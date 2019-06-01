
package services;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import utilities.AbstractTest;
import domain.Activity;
import domain.Enrolment;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class EnrolmentServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private EnrolmentService	enrolmentService;

	@Autowired
	private ActivityService		activityService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.4
	 *         Caso de uso: listar "Enrolments" de la asociación como miembro
	 *         Tests positivos: 1
	 *         *** 1. Listar "Enrolments" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de listar "Enrolments" sin estar logeado
	 *         *** 2. Intento de listar "Enrolments" sin pertenecer a una asociación
	 *         Analisis de cobertura de sentencias: 100% 28/28 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListEnrolmentsMember() {
		final Object testingData[][] = {
			{
				"member1", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"member6", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listEnrolmentsMemberTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.4
	 *         Caso de uso: aceptar una "Enrolment" como miembro
	 *         Tests positivos: 1
	 *         *** 1. Aceptar una "Enrolment" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de aceptar una "Enrolment" sin estar logeado
	 *         *** 2. Intento de aceptar una "Enrolment" que no pertenece a tu asociación
	 *         *** 3. Intento de aceptar una "Enrolment" que no tiene estado 'Pendiente'
	 *         *** 4. Intento de aceptar una "Enrolment" cuya deadline ha pasado
	 *         Analisis de cobertura de sentencias: 99,2% 129/130 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverAcceptEnrolment() {
		final Object testingData[][] = {
			{
				"member1", "enrolment2", null
			}, {
				null, "enrolment1", IllegalArgumentException.class
			}, {
				"member4", "enrolment2", IllegalArgumentException.class
			}, {
				"member1", "enrolment3", IllegalArgumentException.class
			}, {
				"member1", "enrolment1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.acceptEnrolmentTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.4
	 *         Caso de uso: rechazar una "Enrolment" como miembro
	 *         Tests positivos: 1
	 *         *** 1. Rechazar una "Enrolment" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de rechazar una "Enrolment" sin estar logeado
	 *         *** 2. Intento de rechazar una "Enrolment" que no pertenece a tu asociación
	 *         *** 3. Intento de rechazar una "Enrolment" que no tiene estado 'Pendiente'
	 *         *** 4. Intento de rechazar una "Enrolment" cuya deadline ha pasado
	 *         Analisis de cobertura de sentencias: 99,2% 129/130 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeclineEnrolment() {
		final Object testingData[][] = {
			{
				"member1", "enrolment2", null
			}, {
				null, "enrolment1", IllegalArgumentException.class
			}, {
				"member4", "enrolment2", IllegalArgumentException.class
			}, {
				"member1", "enrolment3", IllegalArgumentException.class
			}, {
				"member1", "enrolment1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.declineEnrolmentTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 15.3
	 *         Caso de uso: listar "Enrolments" del visitor logeado
	 *         Tests positivos: 1
	 *         *** 1. Listar "Enrolments" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Enrolments" sin estar logeado
	 *         Analisis de cobertura de sentencias: 100% 23/23 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListEnrolmentsVisitor() {
		final Object testingData[][] = {
			{
				"visitor1", null
			}, {
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listEnrolmentsVisitorTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 15.3
	 *         Caso de uso: crear "Enrolment" para una actividad
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Enrolment" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de creación de un "Enrolment" sin estar logeado
	 *         *** 2. Intento de creación de un "Enrolment" teniendo ya una solicitud pendiente o aceptada para esa actividad
	 *         *** 3. Intento de creación de un "Enrolment" sin poder inscribirse a la actividad seleccionada
	 *         Analisis de cobertura de sentencias: 100% 122/122 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateEnrolment() {
		final Object testingData[][] = {
			{
				"visitor1", "activity7", null
			}, {
				null, "activity7", IllegalArgumentException.class
			}, {
				"visitor1", "activity2", IllegalArgumentException.class
			}, {
				"visitor1", "activity6", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createEnrolmentTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.4
	 *         Caso de uso: cancelar una "Enrolment" como visitante
	 *         Tests positivos: 1
	 *         *** 1. Cancelar una "Enrolment" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de cancelar una "Enrolment" sin estar logeado
	 *         *** 2. Intento de cancelar una "Enrolment" que no pertenece al visitante logeado
	 *         *** 3. Intento de cancelar una "Enrolment" que no tiene estado 'Pendiente' o 'Aceptado'
	 *         *** 4. Intento de cancelar una "Enrolment" cuya deadline ha pasado
	 *         Analisis de cobertura de sentencias: 99,3% 138/139 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCancelEnrolment() {
		final Object testingData[][] = {
			{
				"visitor1", "enrolment2", null
			}, {
				null, "enrolment2", IllegalArgumentException.class
			}, {
				"visitor4", "enrolment2", IllegalArgumentException.class
			}, {
				"visitor4", "enrolment7", IllegalArgumentException.class
			}, {
				"visitor1", "enrolment1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.cancelEnrolmentTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listEnrolmentsMemberTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Enrolment> enrolments;

		super.startTransaction();

		try {
			super.authenticate(username);
			enrolments = this.enrolmentService.findEnrolmentsByAssociationMemberLogged();
			Assert.notNull(enrolments);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void acceptEnrolmentTemplate(final String username, final String enrolment, final Class<?> expected) {
		Class<?> caught = null;
		Enrolment enrolmentEntity;
		enrolmentEntity = this.enrolmentService.findOne(super.getEntityId(enrolment));

		super.startTransaction();

		try {
			super.authenticate(username);
			this.enrolmentService.acceptEnrolment(enrolmentEntity);
			this.enrolmentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void declineEnrolmentTemplate(final String username, final String enrolment, final Class<?> expected) {
		Class<?> caught = null;
		Enrolment enrolmentEntity;
		enrolmentEntity = this.enrolmentService.findOne(super.getEntityId(enrolment));

		super.startTransaction();

		try {
			super.authenticate(username);
			this.enrolmentService.declineEnrolment(enrolmentEntity);
			this.enrolmentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void listEnrolmentsVisitorTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Enrolment> enrolments;

		super.startTransaction();

		try {
			super.authenticate(username);
			enrolments = this.enrolmentService.findEnrolmentsByVisitorLogged();
			Assert.notNull(enrolments);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createEnrolmentTemplate(final String username, final String activity, final Class<?> expected) {
		Class<?> caught = null;
		Enrolment enrolmentEntity;
		final Activity activityEntity = this.activityService.findOne(super.getEntityId(activity));

		super.startTransaction();

		try {
			super.authenticate(username);
			enrolmentEntity = this.enrolmentService.create();
			enrolmentEntity.setActivity(activityEntity);
			this.enrolmentService.save(enrolmentEntity);
			this.enrolmentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void cancelEnrolmentTemplate(final String username, final String enrolment, final Class<?> expected) {
		Class<?> caught = null;
		Enrolment enrolmentEntity;
		enrolmentEntity = this.enrolmentService.findOne(super.getEntityId(enrolment));

		super.startTransaction();

		try {
			super.authenticate(username);
			this.enrolmentService.cancelEnrolment(enrolmentEntity);
			this.enrolmentService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

}
