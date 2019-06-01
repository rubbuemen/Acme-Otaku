
package usecases;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import services.AdministratorService;
import services.SellerService;
import services.StandService;
import utilities.AbstractTest;
import domain.Seller;
import domain.Stand;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class DashboardTest extends AbstractTest {

	// SUT Services
	@Autowired
	private AdministratorService	administratorService;

	@Autowired
	private SellerService			sellerService;

	@Autowired
	private StandService			standService;

	@PersistenceContext
	EntityManager					entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisitos funcionales: 16.4, 36.4, 47.1
	 *         Caso de uso: mostrar una "dashboard"
	 *         Tests positivos: 1
	 *         *** 1. Mostrar una "dashboard" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de muestra de una "dashboard" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 96,03% 242/252 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDashboard() {
		final Object testingData[][] = {
			{
				"admin", "1.6667,0,6,2.1344", "1.2222,0,2,0.6285", "4.883333333333334,0.0,15.3,5.7664017280179936", "0.09091", "0.18182", "0.63636", "0.09091", "0.63636", "1.0,0,4,1.5275", "0.66667,0,2,0.74536", "0.25", "seller1,seller2,seller3",
				"stand1,stand2", "0.16667", null
			},
			{
				"member1", "1.6667,0,6,2.1344", "1.2222,0,2,0.6285", "4.883333333333334,0.0,15.3,5.7664017280179936", "0.09091", "0.18182", "0.63636", "0.09091", "0.63636", "1.0,0,4,1.5275", "0.66667,0,2,0.74536", "0.25", "seller1,seller2,seller3",
				"stand1,stand2", "0.16667", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.dashboardTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (String) testingData[i][5], (String) testingData[i][6],
				(String) testingData[i][7], (String) testingData[i][8], (String) testingData[i][9], (String) testingData[i][10], (String) testingData[i][11], (String) testingData[i][12], (String) testingData[i][13], (String) testingData[i][14],
				(Class<?>) testingData[i][15]);
	}
	// Template methods ------------------------------------------------------

	protected void dashboardTemplate(final String username, final String dashboardQueryC1, final String dashboardQueryC2, final String dashboardQueryC3, final String dashboardQueryC4, final String dashboardQueryC5, final String dashboardQueryC6,
		final String dashboardQueryC7, final String dashboardQueryC8, final String dashboardQueryB1, final String dashboardQueryB2, final String dashboardQueryB3, final String dashboardQueryB4, final String dashboardQueryA1, final String dashboardQueryA2,
		final Class<?> expected) {
		Class<?> caught = null;
		final Collection<Seller> sellers = new HashSet<>();
		final Collection<Stand> stands = new HashSet<>();

		super.startTransaction();

		try {
			super.authenticate(username);

			//Query C1
			Assert.isTrue(dashboardQueryC1.equals(this.administratorService.dashboardQueryC1()));

			//Query C2
			Assert.isTrue(dashboardQueryC2.equals(this.administratorService.dashboardQueryC2()));

			//Query C3
			Assert.isTrue(dashboardQueryC3.equals(this.administratorService.dashboardQueryC3()));

			//Query C4
			Assert.isTrue(dashboardQueryC4.equals(this.administratorService.dashboardQueryC4()));

			//Query C5
			Assert.isTrue(dashboardQueryC5.equals(this.administratorService.dashboardQueryC5()));

			//Query C6
			Assert.isTrue(dashboardQueryC6.equals(this.administratorService.dashboardQueryC6()));

			//Query C7
			Assert.isTrue(dashboardQueryC7.equals(this.administratorService.dashboardQueryC7()));

			//Query C8
			Assert.isTrue(dashboardQueryC8.equals(this.administratorService.dashboardQueryC8()));

			//Query B1
			Assert.isTrue(dashboardQueryB1.equals(this.administratorService.dashboardQueryB1()));

			//Query B2
			Assert.isTrue(dashboardQueryB2.equals(this.administratorService.dashboardQueryB2()));

			//Query B3
			Assert.isTrue(dashboardQueryB3.equals(this.administratorService.dashboardQueryB3()));

			//Query B4
			final String[] queryB4Dashboard = dashboardQueryB4.split(",");
			for (final String seller : queryB4Dashboard) {
				final Seller s = this.sellerService.findOne(super.getEntityId(seller));
				sellers.add(s);
			}

			Assert.isTrue(sellers.containsAll(this.administratorService.dashboardQueryB4()));
			sellers.clear();

			//Query A1
			final String[] queryA1Dashboard = dashboardQueryA1.split(",");
			for (final String stand : queryA1Dashboard) {
				final Stand s = this.standService.findOne(super.getEntityId(stand));
				stands.add(s);
			}

			Assert.isTrue(stands.containsAll(this.administratorService.dashboardQueryA1()));
			stands.clear();

			//Query A2
			Assert.isTrue(dashboardQueryA2.equals(this.administratorService.dashboardQueryA2()));

		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}
}
