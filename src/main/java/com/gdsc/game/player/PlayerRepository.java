package com.gdsc.game.player;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PlayerRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(Player player) {
        em.persist(player);
    }

    public List<Player> findAll() {
        return em.createQuery("select t from Player as t", Player.class)
                .getResultList();
    }

    public Player findByName(String playerName) {
        return em.find(Player.class, playerName);
    }
}
