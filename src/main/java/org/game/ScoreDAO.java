package org.game;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.persistence.EntityManager;

public class ScoreDAO {
    private EntityManager em;
    private static final Logger logger = LogManager.getLogger(ScoreDAO.class);

    public ScoreDAO() {
        em = EntityManagerHelper.getEntityManager();
    }

    public void createScore(Score score) {
        logger.info("Creating score: {}", score);
        em.getTransaction().begin();
        em.persist(score);
        em.getTransaction().commit();
    }

    public void updateScore(Score score) {
        logger.info("Updating score: {}", score);
        em.getTransaction().begin();
        em.merge(score);
        em.getTransaction().commit();
    }

    public void deleteScore(Score score) {
        logger.info("Deleting score: {}", score);
        em.getTransaction().begin();
        em.remove(score);
        em.getTransaction().commit();
    }
}
