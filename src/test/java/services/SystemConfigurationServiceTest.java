
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
import domain.Actor;
import domain.SystemConfiguration;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SystemConfigurationServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private SystemConfigurationService	systemConfigurationService;

	@Autowired
	private ActorService				actorService;

	@PersistenceContext
	EntityManager						entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisitos no funcionales sobre edición de la configuración del sistema
	 *         Caso de uso: editar un "SystemConfiguration"
	 *         Tests positivos: 1
	 *         *** 1. Editar de un "SystemConfiguration" correctamente
	 *         Tests negativos: 13
	 *         *** 1. Intento de edición de "SystemConfiguration" con una autoridad no permitida
	 *         *** 2. Intento de edición de "SystemConfiguration" con nombre vacío
	 *         *** 3. Intento de edición de "SystemConfiguration" con url de banner vacío
	 *         *** 4. Intento de edición de "SystemConfiguration" con url de banner que no es URL
	 *         *** 5. Intento de edición de "SystemConfiguration" con mensaje de bienvenida en inglés vacío
	 *         *** 6. Intento de edición de "SystemConfiguration" con mensaje de bienvenida en español vacío
	 *         *** 7. Intento de edición de "SystemConfiguration" con código telefónico del país vacío
	 *         *** 8. Intento de edición de "SystemConfiguration" con código telefónico del país que no cumple patrón
	 *         *** 9. Intento de edición de "SystemConfiguration" con palabras de spam vacío
	 *         *** 10. Intento de edición de "SystemConfiguration" con marcas de tarjetas de crédito vacío
	 *         *** 11. Intento de edición de "SystemConfiguration" con porcentaje de IVA nulo
	 *         *** 12. Intento de edición de "SystemConfiguration" con porcentaje de IVA menor a 0
	 *         *** 13. Intento de edición de "SystemConfiguration" con porcentaje de IVA mayor a 100
	 *         Analisis de cobertura de sentencias: 100% 20/20 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void driverEditSystemConfiguration() {
		final Collection<String> stringValues = this.stringValues();
		final Collection<String> emptyCollection = new HashSet<>();

		final Object testingData[][] = {
			{
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", stringValues, stringValues, 21.24, null
			}, {
				"member1", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", stringValues, stringValues, 21.24, IllegalArgumentException.class
			}, {
				"admin", "", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", stringValues, stringValues, 21.24, ConstraintViolationException.class
			}, {
				"admin", "testName", "", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", stringValues, stringValues, 21.24, ConstraintViolationException.class
			}, {
				"admin", "testName", "test", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", stringValues, stringValues, 21.24, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "", "testWelcomeMessageES", "+34", stringValues, stringValues, 21.24, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "", "+34", stringValues, stringValues, 21.24, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "", stringValues, stringValues, 21.24, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "test", stringValues, stringValues, 21.24, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", emptyCollection, stringValues, 21.24, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", stringValues, emptyCollection, 21.24, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", stringValues, stringValues, null, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", stringValues, stringValues, -1.0, ConstraintViolationException.class
			}, {
				"admin", "testName", "http://www.testBannerUrl.com", "testWelcomeMessageEN", "testWelcomeMessageES", "+34", stringValues, stringValues, 101.0, ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editSystemConfigurationTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5],
				(Collection<String>) testingData[i][6], (Collection<String>) testingData[i][7], (Double) testingData[i][8], (Class<?>) testingData[i][9]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 36.1
	 *         Caso de uso: listar "Actors" sospechosos
	 *         Tests positivos: 1
	 *         *** 1. Listar "Actors" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Actors" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 14/14 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListActors() {
		final Object testingData[][] = {
			{
				"admin", null
			}, {
				"visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listActorsTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 36.2
	 *         Caso de uso: banear un "Actor"
	 *         Tests positivos: 1
	 *         *** 1. Banear un "Actor" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de banear un "Actor" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 21/21 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverBanActor() {
		final Object testingData[][] = {
			{
				"admin", "visitor1", null
			}, {
				"member1", "visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.banActorTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 36.3
	 *         Caso de uso: desbanear un "Actor"
	 *         Tests positivos: 1
	 *         *** 1. Desbanear un "Actor" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de desbanear un "Actor" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 21/21 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverUnbanActor() {
		final Object testingData[][] = {
			{
				"admin", "visitor1", null
			}, {
				"member1", "visitor1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.unbanActorTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void editSystemConfigurationTemplate(final String username, final String nameSystem, final String bannerUrl, final String welcomeMessageEnglish, final String welcomeMessageSpanish, final String phoneCountryCode,
		final Collection<String> spamWords, final Collection<String> creditCardMakes, final Double VATPercentage, final Class<?> expected) {
		Class<?> caught = null;
		SystemConfiguration systemConfiguration;

		super.startTransaction();

		try {
			super.authenticate(username);
			systemConfiguration = this.systemConfigurationService.getConfiguration();
			systemConfiguration.setNameSystem(nameSystem);
			systemConfiguration.setBannerUrl(bannerUrl);
			systemConfiguration.setWelcomeMessageEnglish(welcomeMessageEnglish);
			systemConfiguration.setWelcomeMessageSpanish(welcomeMessageSpanish);
			systemConfiguration.setPhoneCountryCode(phoneCountryCode);
			systemConfiguration.setSpamWords(spamWords);
			systemConfiguration.setCreditCardMakes(creditCardMakes);
			systemConfiguration.setVATPercentage(VATPercentage);
			this.systemConfigurationService.save(systemConfiguration);
			this.systemConfigurationService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void listActorsTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Actor> actors;

		super.startTransaction();

		try {
			super.authenticate(username);
			actors = this.actorService.findActorsToBan();
			Assert.notNull(actors);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void banActorTemplate(final String username, final String actor, final Class<?> expected) {
		Class<?> caught = null;

		super.startTransaction();

		try {
			super.authenticate(username);
			this.actorService.banActor(this.actorService.findOne(super.getEntityId(actor)));
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void unbanActorTemplate(final String username, final String actor, final Class<?> expected) {
		Class<?> caught = null;

		super.startTransaction();

		try {
			super.authenticate(username);
			this.actorService.unbanActor(this.actorService.findOne(super.getEntityId(actor)));
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	// Auxiliar methods ------------------------------------------------------

	private Collection<String> stringValues() {
		final Collection<String> stringValues = new HashSet<>();
		stringValues.add("test1");
		stringValues.add("test2");
		stringValues.add("test3");
		stringValues.add("test4");
		stringValues.add("test5");

		return stringValues;
	}
}
