package com.gdsc.game.job;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class JobRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Job job) {
        em.persist(job);
    }

    public Job findByName(String name) {
        return em.find(Job.class, name);
    }

}
