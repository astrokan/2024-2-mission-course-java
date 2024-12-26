package com.gdsc.game.action.skill;

import com.gdsc.game.action.Action;
import com.gdsc.game.job.Job;
import com.gdsc.game.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Random;

@Entity
@NoArgsConstructor
@Getter
public class Skill implements Action {
    private static final int MIN_DAMAGE = 1;
    private static final int MAX_DAMAGE = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "skill_id")
    private Long id;

    @Column(name = "skill_name")
    private String name;
    @Column(name = "skill_mpcost")
    private int mpCost;
    @Column(name = "skill_cooldown")
    private int coolDown;

    @ManyToOne
    @JoinColumn(name = "job_name")
    private Job job;      // 해당 스킬을 가진 직업

    public int getMaxDamage() {return mpCost*MAX_DAMAGE;}
    public int getMinDamage() {return mpCost*MIN_DAMAGE;}
    public int getMpCost() { return mpCost; }

    public Skill(String name, int mpCost, int coolDown) {
        this.name = name;
        this.mpCost = mpCost;
        this.coolDown = coolDown;
    }

    public int getSkillDamage() {
        int baseDamage = new Random().nextInt(MAX_DAMAGE- MIN_DAMAGE + 1) + MIN_DAMAGE; // 기본 1~10 데미지
        return baseDamage * mpCost;
    }
    @Override
    public String getName() {
        return name;
    }

    @Override
    public int execute() {
        int damage = getSkillDamage();

        return damage;
    }
}
