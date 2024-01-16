package org.game;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerHelper {
    private static EntityManagerFactory emf;
    private static EntityManager em;

    public static EntityManager getEntityManager() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("org.game");
        }
        if (em == null) {
            em = emf.createEntityManager();
        }
        return em;
    }

    public static void closeEntityManager() {
        if (em != null) {
            em.close();
        }
        if (emf != null) {
            emf.close();
        }
    }
}
