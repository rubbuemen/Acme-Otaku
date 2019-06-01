
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
import domain.Application;
import domain.Association;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ApplicationServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private ApplicationService	applicationService;

	@Autowired
	private AssociationService	associationService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 12.1
	 *         Caso de uso: listar "Applications" del miembro logeado
	 *         Tests positivos: 1
	 *         *** 1. Listar "Applications" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Applications" sin estar logeado
	 *         Analisis de cobertura de sentencias: 100% 23/23 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListApplications() {
		final Object testingData[][] = {
			{
				"member5", null
			}, {
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listApplicationsTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 12.1
	 *         Caso de uso: crear "Application" para unirse a una asociación
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Application" correctamente
	 *         Tests negativos: 6
	 *         *** 1. Intento de creación de un "Application" sin estar logeado
	 *         *** 2. Intento de creación de un "Application" con habilidades vacío
	 *         *** 3. Intento de creación de un "Application" con razón para unirse vacío
	 *         *** 4. Intento de creación de un "Application" perteneciendo ya a una asociación
	 *         *** 5. Intento de creación de un "Application" teniendo ya una solicitud pendiente para esa asociación
	 *         *** 6. Intento de creación de un "Application" sin poder unirse a la asociación seleccionada
	 *         Analisis de cobertura de sentencias: 98,9% 130/131 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateApplication() {
		final Object testingData[][] = {
			{
				"member6", "skillsTest", "reasonToJoinTest", "association1", null
			}, {
				null, "skillsTest", "reasonToJoinTest", "association1", IllegalArgumentException.class
			}, {
				"member6", "", "reasonToJoinTest", "association1", ConstraintViolationException.class
			}, {
				"member6", "skillsTest", "", "association1", ConstraintViolationException.class
			}, {
				"member1", "skillsTest", "reasonToJoinTest", "association1", IllegalArgumentException.class
			}, {
				"member5", "skillsTest", "reasonToJoinTest", "association1", IllegalArgumentException.class
			}, {
				"member6", "skillsTest", "reasonToJoinTest", "association3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createApplicationTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 13.1
	 *         Caso de uso: listar "Applications" de la asociación como presidente
	 *         Tests positivos: 1
	 *         *** 1. Listar "Applications" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de listar "Applications" sin estar logeado
	 *         *** 2. Intento de listar "Applications" sin ser presidente de la asociación
	 *         Analisis de cobertura de sentencias: 100% 29/29 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListApplicationsPresident() {
		final Object testingData[][] = {
			{
				"member1", null
			}, {
				null, IllegalArgumentException.class
			}, {
				"member3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listApplicationsPresidentTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 13.1
	 *         Caso de uso: aceptar una "Application" como presidente
	 *         Tests positivos: 1
	 *         *** 1. Aceptar una "Application" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de aceptar una "Application" sin estar logeado
	 *         *** 2. Intento de aceptar una "Application" sin ser presidente de la asociación
	 *         *** 3. Intento de aceptar una "Application" que no pertenece a tu asociación
	 *         *** 4. Intento de aceptar una "Application" que no tiene estado 'Pendiente'
	 *         Analisis de cobertura de sentencias: 86,9% 86/99 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverAcceptApplicationPresident() {
		final Object testingData[][] = {
			{
				"member1", "application2", null
			}, {
				null, "application2", IllegalArgumentException.class
			}, {
				"member3", "application2", IllegalArgumentException.class
			}, {
				"member4", "application2", IllegalArgumentException.class
			}, {
				"member2", "application1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.acceptApplicationPresidentTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 13.1
	 *         Caso de uso: rechazar una "Application" como presidente
	 *         Tests positivos: 1
	 *         *** 1. Rechazar una "Application" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de rechazar una "Application" sin estar logeado
	 *         *** 2. Intento de rechazar una "Application" sin ser presidente de la asociación
	 *         *** 3. Intento de rechazar una "Application" que no pertenece a tu asociación
	 *         *** 4. Intento de rechazar una "Application" que no tiene estado 'Pendiente'
	 *         Analisis de cobertura de sentencias: 98,3% 58/59 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeclineApplicationPresident() {
		final Object testingData[][] = {
			{
				"member1", "application2", null
			}, {
				null, "application2", IllegalArgumentException.class
			}, {
				"member3", "application2", IllegalArgumentException.class
			}, {
				"member4", "application2", IllegalArgumentException.class
			}, {
				"member2", "application1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.declineApplicationPresidentTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listApplicationsTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Application> applications;

		super.startTransaction();

		try {
			super.authenticate(username);
			applications = this.applicationService.findApplicationsByMemberLogged();
			Assert.notNull(applications);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createApplicationTemplate(final String username, final String skills, final String reasonToJoin, final String association, final Class<?> expected) {
		Class<?> caught = null;
		Application applicationEntity;
		final Association associationEntity = this.associationService.findOne(super.getEntityId(association));

		super.startTransaction();

		try {
			super.authenticate(username);
			applicationEntity = this.applicationService.create();
			applicationEntity.setSkills(skills);
			applicationEntity.setReasonToJoin(reasonToJoin);
			applicationEntity.setAssociation(associationEntity);
			this.applicationService.save(applicationEntity);
			this.applicationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void listApplicationsPresidentTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Application> applications;

		super.startTransaction();

		try {
			super.authenticate(username);
			applications = this.applicationService.findApplicationsByPresidentLogged();
			Assert.notNull(applications);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void acceptApplicationPresidentTemplate(final String username, final String application, final Class<?> expected) {
		Class<?> caught = null;
		Application applicationEntity;
		applicationEntity = this.applicationService.findOne(super.getEntityId(application));

		super.startTransaction();

		try {
			super.authenticate(username);
			this.applicationService.acceptApplication(applicationEntity);
			this.applicationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void declineApplicationPresidentTemplate(final String username, final String application, final Class<?> expected) {
		Class<?> caught = null;
		Application applicationEntity;
		applicationEntity = this.applicationService.findOne(super.getEntityId(application));

		super.startTransaction();

		try {
			super.authenticate(username);
			this.applicationService.declineApplication(applicationEntity);
			this.applicationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

}
