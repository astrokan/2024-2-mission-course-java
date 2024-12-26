package com.gdsc.game.player.skillcooldown;

import com.gdsc.game.action.skill.Skill;
import com.gdsc.game.player.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SkillCooldownRepository {

    @PersistenceContext
    private EntityManager em;

    public List<SkillCooldown> findSkillCooldownByPlayer(Player player) {
        return em.createQuery("SELECT t FROM SkillCooldown t WHERE t.player = :player", SkillCooldown.class)
                .setParameter("player", player)
                .getResultList();
    }

    public void save(SkillCooldown skillCooldown) {
        em.persist(skillCooldown);
    }
    public SkillCooldown findOneByPlayerAndSkill(Player player, Skill skill) {
        return em.createQuery("SELECT t FROM SkillCooldown t WHERE t.player = :player and t.skill = :skill", SkillCooldown.class)
                .setParameter("player", player)
                .setParameter("skill", skill)
                .getSingleResult();
    }
}
