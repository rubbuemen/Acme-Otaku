
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
import domain.Activity;
import domain.Category;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ActivityServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private ActivityService	activityService;

	@Autowired
	private CategoryService	categoryService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.3
	 *         Caso de uso: listar "Activities"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Activities" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de listar "Activities" con una autoridad no permitida
	 *         *** 2. Intento de listar "Activities" sin pertenecer a una asociación
	 *         Analisis de cobertura de sentencias: 100% 27/27 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListActivitiesMember() {
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
			this.listActivitiesMemberTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.3
	 *         Caso de uso: crear una "Activity"
	 *         Tests positivos: 1
	 *         *** 1. Crear una "Activity" correctamente
	 *         Tests negativos: 6
	 *         *** 1. Intento de creación de una "Activity" con una autoridad no permitida
	 *         *** 2. Intento de creación de una "Activity" con nombre vacío
	 *         *** 3. Intento de creación de una "Activity" con descripción vacía
	 *         *** 4. Intento de creación de una "Activity" con fecha final nula
	 *         *** 5. Intento de creación de una "Activity" con fecha final que no es futura
	 *         *** 6. Intento de creación de una "Activity" sin pertenecer a una asociación
	 *         Analisis de cobertura de sentencias: 100% 135/135 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateActivity() {
		final Calendar cal = Calendar.getInstance();
		cal.set(2000, 9, 30);
		final Date datePast = cal.getTime();
		cal.set(2200, 9, 30);
		final Date dateFuture = cal.getTime();
		final Object testingData[][] = {
			{
				"member1", "nameTest", "descriptionTest", dateFuture, "category1", null
			}, {
				"visitor1", "nameTest", "descriptionTest", dateFuture, "category1", IllegalArgumentException.class
			}, {
				"member1", "", "descriptionTest", dateFuture, "category1", ConstraintViolationException.class
			}, {
				"member1", "nameTest", "", dateFuture, "category1", ConstraintViolationException.class
			}, {
				"member1", "nameTest", "descriptionTest", null, "category1", ConstraintViolationException.class
			}, {
				"member1", "nameTest", "descriptionTest", datePast, "category1", IllegalArgumentException.class
			}, {
				"member6", "nameTest", "descriptionTest", dateFuture, "category1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createActivityTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Date) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.3
	 *         Caso de uso: editar una "Activity"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Activity" correctamente
	 *         Tests negativos: 8
	 *         *** 1. Intento de edición de una "Activity" con una autoridad no permitida
	 *         *** 2. Intento de edición de una "Activity" que no es del "Member" logeado
	 *         *** 3. Intento de edición de una "Activity" con nombre vacío
	 *         *** 4. Intento de edición de una "Activity" con descripción vacía
	 *         *** 5. Intento de edición de una "Activity" con fecha final nula
	 *         *** 6. Intento de edición de una "Activity" con fecha final que no es futura
	 *         *** 7. Intento de edición de una "Activity" sin pertenecer a una asociación
	 *         *** 8. Intento de edición de una "Activity" que está en modo final
	 *         Analisis de cobertura de sentencias: 100% 90/90 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */

	@Test
	public void driverEditActivity() {
		final Calendar cal = Calendar.getInstance();
		cal.set(2000, 9, 30);
		final Date datePast = cal.getTime();
		cal.set(2200, 9, 30);
		final Date dateFuture = cal.getTime();
		final Object testingData[][] = {
			{
				"member1", "activity3", "nameTest", "descriptionTest", dateFuture, null
			}, {
				"visitor1", "activity3", "nameTest", "descriptionTest", dateFuture, IllegalArgumentException.class
			}, {
				"member3", "activity3", "nameTest", "descriptionTest", dateFuture, IllegalArgumentException.class
			}, {
				"member1", "activity3", "", "descriptionTest", dateFuture, ConstraintViolationException.class
			}, {
				"member1", "activity3", "nameTest", "", dateFuture, ConstraintViolationException.class
			}, {
				"member1", "activity3", "nameTest", "descriptionTest", null, ConstraintViolationException.class
			}, {
				"member1", "activity3", "nameTest", "descriptionTest", datePast, IllegalArgumentException.class
			}, {
				"member6", "activity3", "nameTest", "descriptionTest", dateFuture, IllegalArgumentException.class
			}, {
				"member1", "activity2", "nameTest", "descriptionTest", dateFuture, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editActivityTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (Date) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.3
	 *         Caso de uso: eliminar un "Activity"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar una "Activity" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de eliminación de una "Activity" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de una "Activity" que no es del "Member" logeado
	 *         *** 3. Intento de eliminación de una "Activity" que está en modo final
	 *         Analisis de cobertura de sentencias: 98,6% 68/69 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteActivity() {
		final Object testingData[][] = {
			{
				"member1", "activity3", null
			}, {
				"visitor1", "activity3", IllegalArgumentException.class
			}, {
				"member4", "activity3", IllegalArgumentException.class
			}, {
				"member1", "activity2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteActivityTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.3
	 *         Caso de uso: cambiar a modo final un "Activity"
	 *         Tests positivos: 1
	 *         *** 1. Cambiar a modo final un "Activity" correctamente
	 *         Tests negativos: 3
	 *         *** 1. Intento de cambiar a modo final un "Activity" con una autoridad no permitida
	 *         *** 2. Intento de cambiar a modo final un "Activity" que no es del "Member" logeado
	 *         *** 3. Intento de cambiar a modo final un "Activity" que ya está en modo final
	 *         Analisis de cobertura de sentencias: 98,3% 57/58 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverChangeFinalModeActivity() {
		final Object testingData[][] = {
			{
				"member1", "activity3", null
			}, {
				"visitor1", "activity3", IllegalArgumentException.class
			}, {
				"member4", "activity3", IllegalArgumentException.class
			}, {
				"member1", "activity2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.changeFinalModeActivityTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listActivitiesMemberTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Activity> activities;

		super.startTransaction();

		try {
			super.authenticate(username);
			activities = this.activityService.findActivitiesByMemberLogged();
			Assert.notNull(activities);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createActivityTemplate(final String username, final String name, final String description, final Date deadline, final String category, final Class<?> expected) {
		Class<?> caught = null;
		final Activity activityEntity;
		final Category categoryEntity = this.categoryService.findOne(super.getEntityId(category));

		super.startTransaction();

		try {
			super.authenticate(username);
			activityEntity = this.activityService.create();
			activityEntity.setName(name);
			activityEntity.setDescription(description);
			activityEntity.setDeadline(deadline);
			activityEntity.setCategory(categoryEntity);
			this.activityService.save(activityEntity);
			this.activityService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editActivityTemplate(final String username, final String activity, final String name, final String description, final Date deadline, final Class<?> expected) {
		Class<?> caught = null;
		final Activity activityEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			activityEntity = this.activityService.findActivityMemberLogged(super.getEntityId(activity));
			activityEntity.setName(name);
			activityEntity.setDescription(description);
			activityEntity.setDeadline(deadline);
			this.activityService.save(activityEntity);
			this.activityService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteActivityTemplate(final String username, final String activity, final Class<?> expected) {
		Class<?> caught = null;
		Activity activityEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			activityEntity = this.activityService.findActivityMemberLogged(super.getEntityId(activity));
			this.activityService.delete(activityEntity);
			this.activityService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void changeFinalModeActivityTemplate(final String username, final String activity, final Class<?> expected) {
		Class<?> caught = null;
		Activity activityEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			activityEntity = this.activityService.findActivityMemberLogged(super.getEntityId(activity));
			this.activityService.changeFinalMode(activityEntity);
			this.activityService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

}
