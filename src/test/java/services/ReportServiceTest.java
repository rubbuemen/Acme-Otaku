
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
import domain.Report;
import domain.Stand;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ReportServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private ReportService	reportService;

	@Autowired
	private StandService	standService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 44.2
	 *         Caso de uso: listar "Reports"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Reports" correctamente sin estar logeado
	 *         Analisis de cobertura de sentencias: 100% 24/24 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListReports() {
		final Object testingData[][] = {
			{
				null
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listReportsTemplate((Class<?>) testingData[i][0]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 45.1
	 *         Caso de uso: listar "Reports"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Reports" correctamente
	 *         Tests negativos: 1
	 *         *** 1. Intento de listar "Reports" con una autoridad no permitida
	 *         Analisis de cobertura de sentencias: 100% 23/23 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListReportsVisitor() {
		final Object testingData[][] = {
			{
				"visitor1", null
			}, {
				"member1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listReportsVisitorTemplate((String) testingData[i][0], (Class<?>) testingData[i][1]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 45.1
	 *         Caso de uso: crear un "Report"
	 *         Tests positivos: 1
	 *         *** 1. Crear un "Report" correctamente
	 *         Tests negativos: 6
	 *         *** 1. Intento de creación de un "Report" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Report" con texto vacío
	 *         *** 3. Intento de creación de un "Report" con puntuación nulo
	 *         *** 4. Intento de creación de un "Report" con puntuación menor a 1
	 *         *** 5. Intento de creación de un "Report" con puntuación mayor a 5
	 *         *** 6. Intento de creación de un "Report" con sumario vacío
	 *         Analisis de cobertura de sentencias: 100% 95/95 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateReport() {
		final Object testingData[][] = {
			{
				"visitor1", "textTest", 3, "stand1", "summaryTest", null
			}, {
				"member1", "textTest", 3, "stand1", "summaryTest", IllegalArgumentException.class
			}, {
				"visitor1", "", 3, "stand1", "summaryTest", ConstraintViolationException.class
			}, {
				"visitor1", "textTest", null, "stand1", "summaryTest", ConstraintViolationException.class
			}, {
				"visitor1", "textTest", 0, "stand1", "summaryTest", ConstraintViolationException.class
			}, {
				"visitor1", "textTest", 6, "stand1", "summaryTest", ConstraintViolationException.class
			}, {
				"visitor1", "textTest", 3, "stand1", "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createReportTemplate((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (String) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 45.1
	 *         Caso de uso: editar un "Report"
	 *         Tests positivos: 1
	 *         *** 1. Editar un "Report" correctamente
	 *         Tests negativos: 7
	 *         *** 1. Intento de edición de un "Report" con una autoridad no permitida
	 *         *** 2. Intento de edición de un "Report" que no es del "Visitor" logeado
	 *         *** 3. Intento de edición de un "Report" con texto vacío
	 *         *** 4. Intento de edición de un "Report" con puntuación nulo
	 *         *** 5. Intento de edición de un "Report" con puntuación menor a 1
	 *         *** 6. Intento de edición de un "Report" con puntuación mayor a 5
	 *         *** 7. Intento de edición de un "Report" con sumario vacío
	 *         Analisis de cobertura de sentencias: 100% 69/69 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverEditReport() {
		final Object testingData[][] = {
			{
				"visitor1", "report1", "textTest", 3, "summaryTest", null
			}, {
				"member1", "report1", "textTest", 3, "summaryTest", IllegalArgumentException.class
			}, {
				"visitor1", "report3", "textTest", 3, "summaryTest", IllegalArgumentException.class
			}, {
				"visitor1", "report1", "", 3, "summaryTest", ConstraintViolationException.class
			}, {
				"visitor1", "report1", "textTest", null, "summaryTest", ConstraintViolationException.class
			}, {
				"visitor1", "report1", "textTest", 0, "summaryTest", ConstraintViolationException.class
			}, {
				"visitor1", "report1", "textTest", 6, "summaryTest", ConstraintViolationException.class
			}, {
				"visitor1", "report1", "textTest", 3, "", ConstraintViolationException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.editReportTemplate((String) testingData[i][0], (String) testingData[i][1], (String) testingData[i][2], (Integer) testingData[i][3], (String) testingData[i][4], (Class<?>) testingData[i][5]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 45.1
	 *         Caso de uso: eliminar un "Report"
	 *         Tests positivos: 1
	 *         *** 1. Eliminar un "Report" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de eliminación de un "Report" con una autoridad no permitida
	 *         *** 2. Intento de eliminación de un "Report" que no es de la "Visitor" logeada
	 *         Analisis de cobertura de sentencias: 98,3% 59/59 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverDeleteReport() {
		final Object testingData[][] = {
			{
				"visitor1", "report1", null
			}, {
				"member1", "report1", IllegalArgumentException.class
			}, {
				"visitor1", "report3", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.deleteReportTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	// Template methods ------------------------------------------------------

	protected void listReportsTemplate(final Class<?> expected) {
		Class<?> caught = null;
		Collection<Report> reports;

		super.startTransaction();

		try {
			reports = this.reportService.findAll();
			Assert.notNull(reports);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void listReportsVisitorTemplate(final String username, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Report> reports;

		super.startTransaction();

		try {
			super.authenticate(username);
			reports = this.reportService.findReportsByVisitorLogged();
			Assert.notNull(reports);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createReportTemplate(final String username, final String text, final Integer score, final String stand, final String summary, final Class<?> expected) {
		Class<?> caught = null;
		Report reportEntity;
		Stand standEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			reportEntity = this.reportService.create();
			standEntity = this.standService.findOne(super.getEntityId(stand));
			reportEntity.setText(text);
			reportEntity.setScore(score);
			reportEntity.setSummary(summary);
			reportEntity.setStand(standEntity);
			this.reportService.save(reportEntity);
			this.reportService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void editReportTemplate(final String username, final String report, final String text, final Integer score, final String summary, final Class<?> expected) {
		Class<?> caught = null;
		Report reportEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			reportEntity = this.reportService.findReportVisitorLogged(super.getEntityId(report));
			reportEntity.setText(text);
			reportEntity.setScore(score);
			reportEntity.setSummary(summary);
			this.reportService.save(reportEntity);
			this.reportService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void deleteReportTemplate(final String username, final String report, final Class<?> expected) {
		Class<?> caught = null;
		Report reportEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			reportEntity = this.reportService.findReportVisitorLogged(super.getEntityId(report));
			this.reportService.delete(reportEntity);
			this.reportService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

}
