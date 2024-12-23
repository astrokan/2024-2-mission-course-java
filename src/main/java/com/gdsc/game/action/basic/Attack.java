package com.gdsc.game.action.basic;

import com.gdsc.game.player.Player;

import java.util.Random;

public class Attack implements BasicAction{
    private static final int MIN_DAMAGE = 1;
    private static final int MAX_DAMAGE = 10;
    private String name;

    public Attack(String name) {
        this.name = name;
    }

    public int getMaxDamage() {return MAX_DAMAGE;}
    public int getMinDamage() {return MIN_DAMAGE;}


    @Override
    public String getName() {return name;}
    public int getRandomDamage() {
        return new Random().nextInt(MAX_DAMAGE - MIN_DAMAGE + 1) + MIN_DAMAGE;
    }

    @Override
    public int execute(Player user, Player target) {
        return getRandomDamage();
    }
}
