package com.gdsc.game.player.skillcooldown;

import com.gdsc.game.action.skill.Skill;
import com.gdsc.game.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class SkillCooldown {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player player;  // 플레이어

    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;    // 스킬

    @Column(name = "remaining_cooldown")
    private int remainingCooldown; // 남은 쿨타임

    public SkillCooldown(Player player, Skill skill) {
        this.player = player;
        this.skill = skill;
        this.remainingCooldown = 0;
    }

    public boolean canUseSkill() { // 스킬 쿨타임 찼는지 여부 확인, 불린형 리턴
        return remainingCooldown <= 0;
    }
    public void updateRemainingCooldown() {
        if (remainingCooldown > 0) {
            this.remainingCooldown -= 1;  // 쿨타임 1 감소
        }
    }
    public void startSkillCooldown() {
        remainingCooldown = skill.getCoolDown();
    }

    public void initializeCooldown() {
        remainingCooldown = 0;
    }
}
