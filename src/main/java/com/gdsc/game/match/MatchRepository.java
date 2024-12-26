package com.gdsc.game.match;

import com.gdsc.game.player.Player;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MatchRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Match match) {
        em.persist(match);
    }

    public Match findById(Long id) {
        return em.find(Match.class, id);
    }

    public List<Match> findAll() {
        return em.createQuery("select t from Match as t", Match.class)
                .getResultList();
    }

    public void delete(Match match) {
        em.remove(match);
    }
}
