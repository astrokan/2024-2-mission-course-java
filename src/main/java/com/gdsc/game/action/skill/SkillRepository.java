package com.gdsc.game.action.skill;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class SkillRepository {

    @PersistenceContext
    private EntityManager em;

    public Skill findById(Long id) {
        return em.find(Skill.class, id);
    }

}
