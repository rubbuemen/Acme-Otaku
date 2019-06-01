
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
import domain.CreditCard;
import domain.Sponsorship;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class SponsorshipServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private SponsorshipService	sponsorshipService;

	@Autowired
	private EventService		eventService;

	@Autowired
	private SponsorService		sponsorService;

	@PersistenceContext
	EntityManager				entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 46.1
	 *         Caso de uso: listar "Sponsorships" logeado como Sponsor
	 *         Tests positivos: 1
	 *         *** 1. Listar "Sponsorships" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Sponsorships" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 23/23 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListSponsorships() {
		final Object testingData[][] = {
			{
				"sponsor1", null
			}, {
				"member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listSponsorshipsTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 46.1
	 *         Caso de uso: crear un "Sponsorship"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Sponsorship" correctamente
	 *         Tests negativos: 21
	 *         *** 1. Intento de creación de un "Sponsorship" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Sponsorship" con banner vacío
	 *         *** 3. Intento de creación de un "Sponsorship" con banner que no es URL
	 *         *** 4. Intento de creación de un "Sponsorship" con página de destino vacío
	 *         *** 5. Intento de creación de un "Sponsorship" con página de destino que no es URL
	 *         *** 6. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene el propietario vacío
	 *         *** 7. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene la marca vacío
	 *         *** 8. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene el número vacío
	 *         *** 9. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un número que no es de tarjeta de crédito
	 *         *** 10. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene el mes de caducidad nulo
	 *         *** 11. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un mes de caducidad menor a 1
	 *         *** 12. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un mes de caducidad mayor a 12
	 *         *** 13. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene el año de caducidad nulo
	 *         *** 14. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un año de caducidad menor a 0
	 *         *** 15. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un año de caducidad mayor a 99
	 *         *** 16. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene el CVV nulo
	 *         *** 17. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un CVV menor a 100
	 *         *** 18. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un CVV mayor a 999
	 *         *** 19. Intento de creación de un "Sponsorship" con un "Event" no disponible
	 *         *** 20. Intento de creación de un "Sponsorship" cuya "CreditCard" tiene un número no numérico
	 *         *** 21. Intento de creación de un "Sponsorship" cuya "CreditCard" está caducada
	 *         Analisis de cobertura de sentencias: 100% 134/134 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateSponsorship() {
		final Object testingData[][] = {
			{
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, null
			}, {
				"member1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, IllegalArgumentException.class
			}, {
				"sponsor1", "event1", "", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "test", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "test", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "8534634734746", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", null, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 0, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 13, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, null, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, -1, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 100, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, null, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 99, ConstraintViolationException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 1000, ConstraintViolationException.class
			}, {
				"sponsor1", "event4", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, IllegalArgumentException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "testCreditCardNumber", 10, 25, 535, IllegalArgumentException.class
			}, {
				"sponsor1", "event1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 2, 19, 535, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createSponsorshipTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Integer) testingData[i][7], (Integer) testingData[i][8], (Integer) testingData[i][9], (Class<?>) testingData[i][10]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 46.1
	 *         Caso de uso: editar un "Sponsorship"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Sponsorship" correctamente
	 *         Tests negativos: 21
	 *         *** 1. Intento de edición de un "Sponsorship" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Sponsorship" que no es del "Sponsor" logeado
	 *         *** 3. Intento de edición de un "Sponsorship" con banner vacío
	 *         *** 4. Intento de edición de un "Sponsorship" con banner que no es URL
	 *         *** 5. Intento de edición de un "Sponsorship" con página de destino vacío
	 *         *** 6. Intento de edición de un "Sponsorship" con página de destino que no es URL
	 *         *** 7. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene el propietario vacío
	 *         *** 8. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene la marca vacío
	 *         *** 9. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene el número vacío
	 *         *** 10. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un número que no es de tarjeta de crédito
	 *         *** 11. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene el mes de caducidad nulo
	 *         *** 12. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un mes de caducidad menor a 1
	 *         *** 13. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un mes de caducidad mayor a 12
	 *         *** 14. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene el año de caducidad nulo
	 *         *** 15. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un año de caducidad menor a 0
	 *         *** 16. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un año de caducidad mayor a 99
	 *         *** 17. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene el CVV nulo
	 *         *** 18. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un CVV menor a 100
	 *         *** 19. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un CVV mayor a 999
	 *         *** 20. Intento de edición de un "Sponsorship" cuya "CreditCard" tiene un número no numérico
	 *         *** 21. Intento de edición de un "Sponsorship" cuya "CreditCard" está caducada
	 *         Analiis de cobertura de sentencias: 100% 114/114 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditSponsorship() {
		final Object testingData[][] = {
			{
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, null
			}, {
				"member1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, IllegalArgumentException.class
			}, {
				"sponsor3", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, IllegalArgumentException.class
			}, {
				"sponsor1", "sponsorship1", "", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "test", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "test", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "", "VISA", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "", "4739158676192764", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "8534634734746", 10, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", null, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 0, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 13, 25, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, null, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, -1, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 100, 535, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, null, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 99, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 10, 25, 1000, ConstraintViolationException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "testCreditCardNumber", 10, 25, 535, IllegalArgumentException.class
			}, {
				"sponsor1", "sponsorship1", "http://www.testBanner.com", "http://www.testTargetPage.com", "testCreditCardHolder", "VISA", "4739158676192764", 2, 19, 535, IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editSponsorshipTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(Integer) testingData[i][7], (Integer) testingData[i][8], (Integer) testingData[i][9], (Class<?>) testingData[i][10]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 46.1
	 *         Caso de uso: eliminar un "Sponsorship"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Sponsorship" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de eliminación de un "Sponsorship" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Sponsorship" que no es del "Sponsor" logeado
	 *         Analisis de cobertura de sentencias: 98,7% 77/78 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteSponsorship() {
		final Object testingData[][] = {
			{
				"sponsor1", "sponsorship1", null
			}, {
				"member1", "sponsorship1", IllegalArgumentException.class
			}, {
				"sponsor3", "sponsorship1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteSponsorshipTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listSponsorshipsTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Sponsorship> sponsorships;

		super.startTransaction();

		try {
			super.authenticate(username);
			sponsorships = this.sponsorshipService.findSponsorshipsBySponsorLogged();
			Assert.notNull(sponsorships);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createSponsorshipTemplate(final String username, final String event, final String banner, final String targetURL, final String creditCardHolder, final String creditCardMake, final String creditCardNumber,
		final Integer creditCardExpirationMonth, final Integer creditCardExpirationYear, final Integer creditCardCVV, final Class<?> expected) {
		Class<?> caught = null;
		Sponsorship sponsorship;

		super.startTransaction();

		try {
			super.authenticate(username);
			sponsorship = this.sponsorshipService.create();
			final CreditCard creditCard = new CreditCard();
			creditCard.setHolder(creditCardHolder);
			creditCard.setMake(creditCardMake);
			creditCard.setNumber(creditCardNumber);
			creditCard.setExpirationMonth(creditCardExpirationMonth);
			creditCard.setExpirationYear(creditCardExpirationYear);
			creditCard.setCvv(creditCardCVV);
			sponsorship.setBanner(banner);
			sponsorship.setTargetURL(targetURL);
			sponsorship.setCreditCard(creditCard);
			sponsorship.setEvent(this.eventService.findOne(super.getEntityId(event)));
			sponsorship.setSponsor(this.sponsorService.findOne(super.getEntityId(username)));
			this.sponsorshipService.save(sponsorship);
			this.sponsorshipService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editSponsorshipTemplate(final String username, final String sponsorship, final String banner, final String targetURL, final String creditCardHolder, final String creditCardMake, final String creditCardNumber,
		final Integer creditCardExpirationMonth, final Integer creditCardExpirationYear, final Integer creditCardCVV, final Class<?> expected) {
		Class<?> caught = null;
		Sponsorship sponsorshipEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			sponsorshipEntity = this.sponsorshipService.findSponsorshipSponsorLogged(super.getEntityId(sponsorship));
			final CreditCard creditCard = new CreditCard();
			creditCard.setHolder(creditCardHolder);
			creditCard.setMake(creditCardMake);
			creditCard.setNumber(creditCardNumber);
			creditCard.setExpirationMonth(creditCardExpirationMonth);
			creditCard.setExpirationYear(creditCardExpirationYear);
			creditCard.setCvv(creditCardCVV);
			sponsorshipEntity.setBanner(banner);
			sponsorshipEntity.setTargetURL(targetURL);
			sponsorshipEntity.setCreditCard(creditCard);
			sponsorshipEntity.setSponsor(this.sponsorService.findOne(super.getEntityId(username)));
			this.sponsorshipService.save(sponsorshipEntity);
			this.sponsorshipService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteSponsorshipTemplate(final String username, final String sponsorship, final Class<?> expected) {
		Class<?> caught = null;
		Sponsorship sponsorshipEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			sponsorshipEntity = this.sponsorshipService.findSponsorshipSponsorLogged(super.getEntityId(sponsorship));
			this.sponsorshipService.delete(sponsorshipEntity);
			this.sponsorshipService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
