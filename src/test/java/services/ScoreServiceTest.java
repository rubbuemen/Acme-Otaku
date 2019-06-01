
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
import domain.Activity;
import domain.Score;

@ContextConfiguration(locations = {
	"classpath:spring/junit.xml"
})
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public class ScoreServiceTest extends AbstractTest {

	// SUT Services
	@Autowired
	private ScoreService	scoreService;

	@Autowired
	private ActivityService	activityService;

	@PersistenceContext
	EntityManager			entityManager;


	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 15.4
	 *         Caso de uso: listar "Scores" de una "Activity"
	 *         Tests positivos: 1
	 *         *** 1. Listar "Scores" de una "Activity" correctamente
	 *         Tests negativos: 2
	 *         *** 1. Intento de listar "Scores" con una autoridad no permitida
	 *         *** 2. Intento de listar "Scores" de una actividad en la que no ha participado
	 *         Analisis de cobertura de sentencias: 97,2% 35/36 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverListScores() {
		final Object testingData[][] = {
			{
				"visitor3", "activity4", null
			}, {
				"member1", "activity4", IllegalArgumentException.class
			}, {
				"visitor3", "activity1", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.listScoresTemplate((String) testingData[i][0], (String) testingData[i][1], (Class<?>) testingData[i][2]);
	}

	/**
	 * @author Rubén Bueno
	 *         Requisito funcional: 15.4
	 *         Caso de uso: crear un "Score" en un "Activity"
	 *         Tests positivos: 2
	 *         *** 1. Crear un "Score" en un "Activity" correctamente
	 *         Tests negativos: 7
	 *         *** 1. Intento de creación de un "Score" en un "Activity" con una autoridad no permitida
	 *         *** 2. Intento de creación de un "Score" de una actividad en la que no ha participado
	 *         *** 3. Intento de creación de un "Score" cuya puntuación es nula
	 *         *** 4. Intento de creación de un "Score" cuya puntuación es menor a 1
	 *         *** 5. Intento de creación de un "Score" cuya puntuación es mayor a 5
	 *         *** 6. Intento de creación de un "Score" cuyo comentarios está vacío
	 *         *** 7. Intento de creación de un "Score" cuya actividad no ha acabado
	 *         Analisis de cobertura de sentencias: 98,3% 74/75 instrucciones
	 *         Analisis de cobertura de datos: alto
	 */
	@Test
	public void driverCreateScore() {
		final Object testingData[][] = {
			{
				"visitor3", "activity4", 3, "commentsTest", null
			}, {
				"member3", "activity4", 3, "commentsTest", IllegalArgumentException.class
			}, {
				"visitor3", "activity1", 3, "commentsTest", IllegalArgumentException.class
			}, {
				"visitor3", "activity4", null, "commentsTest", ConstraintViolationException.class
			}, {
				"visitor3", "activity4", -1, "commentsTest", ConstraintViolationException.class
			}, {
				"visitor3", "activity4", 6, "commentsTest", ConstraintViolationException.class
			}, {
				"visitor3", "activity4", 3, "", ConstraintViolationException.class
			}, {
				"visitor3", "activity5", 3, "commentsTest", IllegalArgumentException.class
			}
		};

		for (int i = 0; i < testingData.length; i++)
			this.createScoreTemplate((String) testingData[i][0], (String) testingData[i][1], (Integer) testingData[i][2], (String) testingData[i][3], (Class<?>) testingData[i][4]);
	}

	// Template methods ------------------------------------------------------

	protected void listScoresTemplate(final String username, final String activity, final Class<?> expected) {
		Class<?> caught = null;
		Collection<Score> scores;

		super.startTransaction();

		try {
			super.authenticate(username);
			scores = this.scoreService.findScoresByActivityId(super.getEntityId(activity));
			Assert.notNull(scores);
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

	protected void createScoreTemplate(final String username, final String activity, final Integer ranking, final String comments, final Class<?> expected) {
		Class<?> caught = null;
		Score score;
		Activity activityEntity;

		super.startTransaction();

		try {
			super.authenticate(username);
			activityEntity = this.activityService.findOne(super.getEntityId(activity));
			score = this.scoreService.create();
			score.setRanking(ranking);
			score.setComments(comments);
			score = this.scoreService.save(score, activityEntity);
			this.scoreService.flush();
		} catch (final Throwable oops) {
			caught = oops.getClass();
			this.entityManager.clear();
		}

		this.checkExceptions(expected, caught);
		super.unauthenticate();
		super.rollbackTransaction();
	}

}
