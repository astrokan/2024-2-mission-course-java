package com.gdsc.game.player;

import com.gdsc.game.job.Job;
import com.gdsc.game.match.Match;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class Player {

    private final int BASIC_HP = 30;
    private final int BASIC_MP = 10;

    @Id
    @Column(name = "player_name")
    private String name;

    @Column(name = "player_hp")
    private int hp;

    @Column(name = "player_mp")
    private int mp;

    @Column(name = "player_level")
    private int level;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;          // 직업

    private void calculateHp() {
        this.hp = BASIC_HP + this.level * 10;
    }
    private void calculateMp() {
        this.mp = BASIC_MP + this.level * 5;
    }

    public Player(String name, Job job, int level) {
        this.name = name;
        this.job = job;
        this.level = level;
        calculateHp();
        calculateMp();
    }

    public boolean hasSameName(String playerName) {
        return this.name.equals(playerName);
    }
    public String printState() {
        return name + " 체력: " + hp + ", 마나: " + mp;
    }
    public boolean isAlive() {
        return hp > 0;
    }

    public void applyDamage(int damage) {this.hp = Math.max(0, hp - damage);}
    public void consumeMp(int mpCost) {this.mp = Math.max(0, mp - mpCost);}
}
