
package services;

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
import domain.Association;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class AssociationServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private AssociationService	associationService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 12.1
	 *         Caso de uso: mostrar "Association"
	 *         Tests positivos: 1
	 *         *** 1. Mostrar "Association" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de mostrar "Association" sin pertenecer a una
	 *         Analisis de cobertura de sentencias: 100% 21/21 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverShowAssociation() {
		final Object testingData[][] = {
			{
				"member1", null
			}, {
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.showAssociationTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 12.1
	 *         Caso de uso: crear una "Association"
	 *         Tests positivos: 1
	 *         *** 1. Crear una "Association" correctamente
	 *         Tests negativos: 9
	 *         *** 1. Intento de creación de una "Association" sin estar logeado
	 *         *** 2. Intento de creación de una "Association" con nombre vacío
	 *         *** 3. Intento de creación de una "Association" con descripción vacío
	 *         *** 4. Intento de creación de una "Association" con slogan vacío
	 *         *** 5. Intento de creación de una "Association" con logo vacío
	 *         *** 6. Intento de creación de una "Association" con logo que no es URL
	 *         *** 7. Intento de creación de una "Association" con color representativo vacío
	 *         *** 8. Intento de creación de una "Association" con color representativo que no cumple el patrón
	 *         *** 9. Intento de creación de una "Association" perteneciendo ya a una
	 *         Analisis de cobertura de sentencias: 100% 94/94 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateAssociation() {
		final Object testingData[][] = {
			{
				"member6", "nameTest", "descriptionTest", "sloganTest", "http://www.logoTest.com", "#FFFFFF", null
			}, {
				null, "nameTest", "descriptionTest", "sloganTest", "http://www.logoTest.com", "#FFFFFF", IllegalArgumentException.class
			}, {
				"member6", "", "descriptionTest", "sloganTest", "http://www.logoTest.com", "#FFFFFF", ConstraintViolationException.class
			}, {
				"member6", "nameTest", "", "sloganTest", "http://www.logoTest.com", "#FFFFFF", ConstraintViolationException.class
			}, {
				"member6", "nameTest", "descriptionTest", "", "http://www.logoTest.com", "#FFFFFF", ConstraintViolationException.class
			}, {
				"member6", "nameTest", "descriptionTest", "sloganTest", "", "#FFFFFF", ConstraintViolationException.class
			}, {
				"member6", "nameTest", "descriptionTest", "sloganTest", "ogoTest", "#FFFFFF", ConstraintViolationException.class
			}, {
				"member6", "nameTest", "descriptionTest", "sloganTest", "http://www.logoTest.com", "", ConstraintViolationException.class
			}, {
				"member6", "nameTest", "descriptionTest", "sloganTest", "http://www.logoTest.com", "representativeColorTest", ConstraintViolationException.class
			}, {
				"member1", "nameTest", "descriptionTest", "sloganTest", "http://www.logoTest.com", "#FFFFFF", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createAssociationTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (Class<?>) testingData[i][6]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 13.2
	 *         Caso de uso: editar una "Association"
	 *         Tests positivos: 1
	 *         *** 1. Editar una "Association" correctamente
	 *         Tests negativos: 10
	 *         *** 1. Intento de edición de una "Association" sin estar logeado
	 *         *** 2. Intento de edición de una "Association" con nombre vacío
	 *         *** 3. Intento de edición de una "Association" con descripción vacío
	 *         *** 4. Intento de edición de una "Association" con slogan vacío
	 *         *** 5. Intento de edición de una "Association" con logo vacío
	 *         *** 6. Intento de edición de una "Association" con logo que no es URL
	 *         *** 7. Intento de edición de una "Association" con color representativo vacío
	 *         *** 8. Intento de edición de una "Association" con color representativo que no cumple el patrón
	 *         *** 9. Intento de edición de una "Association" sin ser presidente de la asociación
	 *         *** 10. Intento de edición de una "Association" sin pertenecer a esa asociación
	 *         Analisis de cobertura de sentencias: 100% 60/60 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditAssociation() {
		final Object testingData[][] = {
			{
				"member1", "association1", "nameTest", "descriptionTest", "sloganTest", "http://www.logoTest.com", "#FFFFFF", null
			}, {
				null, "association1", "nameTest", "descriptionTest", "sloganTest", "http://www.logoTest.com", "#FFFFFF", IllegalArgumentException.class
			}, {
				"member1", "association1", "", "descriptionTest", "sloganTest", "http://www.logoTest.com", "#FFFFFF", ConstraintViolationException.class
			}, {
				"member1", "association1", "nameTest", "", "sloganTest", "http://www.logoTest.com", "#FFFFFF", ConstraintViolationException.class
			}, {
				"member1", "association1", "nameTest", "descriptionTest", "", "http://www.logoTest.com", "#FFFFFF", ConstraintViolationException.class
			}, {
				"member1", "association1", "nameTest", "descriptionTest", "sloganTest", "", "#FFFFFF", ConstraintViolationException.class
			}, {
				"member1", "association1", "nameTest", "descriptionTest", "sloganTest", "ogoTest", "#FFFFFF", ConstraintViolationException.class
			}, {
				"member1", "association1", "nameTest", "descriptionTest", "sloganTest", "http://www.logoTest.com", "", ConstraintViolationException.class
			}, {
				"member1", "association1", "nameTest", "descriptionTest", "sloganTest", "http://www.logoTest.com", "representativeColorTest", ConstraintViolationException.class
			}, {
				"member3", "association2", "nameTest", "descriptionTest", "sloganTest", "http://www.logoTest.com", "#FFFFFF", IllegalArgumentException.class
			}, {
				"member1", "association2", "nameTest", "descriptionTest", "sloganTest", "http://www.logoTest.com", "#FFFFFF", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editAssociationTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Class<?>) testingData[i][7]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 13.2
	 *         Caso de uso: permitir unirse a una "Association" como presidente
	 *         Tests positivos: 1
	 *         *** 1. Permitir unirse a una "Association" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de permitir unirse a una "Association" sin estar logeado
	 *         *** 2. Intento de permitir unirse a una "Association" sin ser presidente de la asociación
	 *         *** 3. Intento de permitir unirse a una "Association" sin ser tu asociación
	 *         *** 4. Intento de permitir unirse a una "Association" que ya permite unirse
	 *         Analisis de cobertura de sentencias: 98,4% 61/62 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverAllowToJoinAssociationPresident() {
		final Object testingData[][] = {
			{
				"member4", "association3", null
			}, {
				null, "association3", IllegalArgumentException.class
			}, {
				"member3", "association3", IllegalArgumentException.class
			}, {
				"member1", "association2", IllegalArgumentException.class
			}, {
				"member2", "association2", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.allowToJoinAssociationPresidentTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 13.2
	 *         Caso de uso: no permitir unirse a una "Association" como presidente
	 *         Tests positivos: 1
	 *         *** 1. No permitir unirse a una "Association" correctamente
	 *         Tests negativos: 4
	 *         *** 1. Intento de no permitir unirse a una "Association" sin estar logeado
	 *         *** 2. Intento de no permitir unirse a una "Association" sin ser presidente de la asociación
	 *         *** 3. Intento de no permitir unirse a una "Association" sin ser tu asociación
	 *         *** 4. Intento de no permitir unirse a una "Association" que ya no permite unirse
	 *         Analisis de cobertura de sentencias: 98,3% 57/58 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverNotAllowToJoinAssociationPresident() {
		final Object testingData[][] = {
			{
				"member1", "association1", null
			}, {
				null, "association1", IllegalArgumentException.class
			}, {
				"member3", "association2", IllegalArgumentException.class
			}, {
				"member1", "association2", IllegalArgumentException.class
			}, {
				"member4", "association3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.notAllowToJoinAssociationPresidentTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 14.5
	 *         Caso de uso: dejar una "Association"
	 *         Tests positivos: 2
	 *         *** 1. Dejar una "Association" correctamente siendo un miembro normal
	 *         *** 2. Dejar una "Association" correctamente siendo el último miembro
	 *         Tests negativos: 1
	 *         *** 1. Intento de dejar "Association" sin ser miembro
	 *         Analisis de cobertura de sentencias: 70,9% 353/498 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverLeaveAssociation() {
		final Object testingData[][] = {
			{
				"member3", null
			}, {
				"member1", null
			}, {
				null, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.leaveAssociationTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	// Template methods ------------------------------------------------------

	protected void showAssociationTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Association association;

		super.startTransaction();

		try {
			super.authenticate(username);
			association = this.associationService.findAssociationByMemberLogged();
			Assert.notNull(association);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createAssociationTemplate(final String username, final String name, final String description, final String slogan, final String logo, final String representativeColor, final Class<?> expected) {
		Class<?> caught = null;
		Association associationEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			associationEntity = this.associationService.create();
			associationEntity.setName(name);
			associationEntity.setDescription(description);
			associationEntity.setSlogan(slogan);
			associationEntity.setLogo(logo);
			associationEntity.setRepresentativeColor(representativeColor);
			this.associationService.save(associationEntity);
			this.associationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editAssociationTemplate(final String username, final String association, final String name, final String description, final String slogan, final String logo, final String representativeColor, final Class<?> expected) {
		Class<?> caught = null;
		final Association associationEntity = this.associationService.findOne(super.getEntityId(association));

		super.startTransaction();

		try {
			super.authenticate(username);
			associationEntity.setName(name);
			associationEntity.setDescription(description);
			associationEntity.setSlogan(slogan);
			associationEntity.setLogo(logo);
			associationEntity.setRepresentativeColor(representativeColor);
			this.associationService.save(associationEntity);
			this.associationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void allowToJoinAssociationPresidentTemplate(final String username, final String association, final Class<?> expected) {
		Class<?> caught = null;
		Association associationEntity;
		associationEntity = this.associationService.findOne(super.getEntityId(association));

		super.startTransaction();

		try {
			super.authenticate(username);
			this.associationService.allowMembers(associationEntity);
			this.associationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void notAllowToJoinAssociationPresidentTemplate(final String username, final String association, final Class<?> expected) {
		Class<?> caught = null;
		Association associationEntity;
		associationEntity = this.associationService.findOne(super.getEntityId(association));

		super.startTransaction();

		try {
			super.authenticate(username);
			this.associationService.notAllowMembers(associationEntity);
			this.associationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void leaveAssociationTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;

		super.startTransaction();

		try {
			super.authenticate(username);
			this.associationService.leave(null);
			this.associationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
