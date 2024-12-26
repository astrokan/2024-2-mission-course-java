package com.gdsc.game.player;

import com.gdsc.game.action.Action;
import com.gdsc.game.action.basic.Attack;
import com.gdsc.game.action.basic.BasicAction;
import com.gdsc.game.action.basic.Defense;
import com.gdsc.game.action.skill.Skill;
import com.gdsc.game.common.exception.JobNotFoundException;
import com.gdsc.game.job.Job;
import com.gdsc.game.job.JobRepository;
import com.gdsc.game.player.skillcooldown.SkillCooldown;
import com.gdsc.game.player.skillcooldown.SkillCooldownRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;
    private final JobRepository jobRepository;
    private final SkillCooldownRepository skillCooldownRepository;

    @Transactional
    public String createPlayer(String playerName, String jobName, int level) {

        Job job = jobRepository.findByName(jobName);
        if (job == null) {
            throw new JobNotFoundException("job not found");
        }

        Player player = new Player(playerName, job, level);
        playerRepository.save(player);

        // 플레이어의 고유 스킬 쿨타임 초기화
        List<Skill> skills = job.getSkills();
        for (Skill skill : skills) {
            SkillCooldown skillCooldown = new SkillCooldown(player, skill);
            skillCooldownRepository.save(skillCooldown);
        }
        return player.getName();
    }

    @Transactional
    public String printPlayersState() {
        List<Player> players = playerRepository.findAll();

        Player player1 = players.get(0);
        Player player2 = players.get(1);
        String player1State = player1.printState();
        String player2State = player2.printState();

        return player1State + "\n\n" + player2State;
    }

    @Transactional
    public String printActionList(String playerName) {
        // 쿨타임 스캔
        Player player = playerRepository.findByName(playerName);
        List<Action> actions = createActionList(playerName);

        int i=1;
        for (Action action : actions) {
            if (action instanceof BasicAction) {
                BasicAction basicAction = (BasicAction) action;
                return (i + ". " + basicAction.getName() + "(" + basicAction.getMinDamage()
                        + " ~ " + basicAction.getMaxDamage() + ")");
            }
            else if (action instanceof Skill) {
                Skill skill = (Skill) action;
                SkillCooldown skillCooldown = skillCooldownRepository.findOneByPlayerAndSkill(player, skill);
                return (i + ". " + skill.getName() + "(" + skill.getMinDamage()
                        + " ~ " + skill.getMaxDamage() + ")" + " - " + skill.getMpCost() + "MP - 남은 쿨타임: "
                        + skillCooldown.getRemainingCooldown() + "턴");
            }
            i++;
        }
        return null;
    }

    @Transactional
    public List<Action> createActionList(String playerName) {
        List<Action> actions = new ArrayList<>();
        Player player = playerRepository.findByName(playerName);
        Job job = jobRepository.findByName(player.getJob().getName());

        Attack attack = new Attack("공격");
        Defense defense = new Defense("방어");
        actions.add(attack);
        actions.add(defense);

        List<Skill> skills = job.getSkills();
        for (Skill skill : skills) {
            actions.add(skill);
        }
        return actions;
    }

    @Transactional
    public List<Action> createActionList(Player player) {
        List<Action> actions = new ArrayList<>();
        Job job = jobRepository.findByName(player.getJob().getName());

        Attack attack = new Attack("공격");
        Defense defense = new Defense("방어");
        actions.add(attack);
        actions.add(defense);

        List<Skill> skills = job.getSkills();
        for (Skill skill : skills) {
            actions.add(skill);
        }
        return actions;
    }

    @Transactional
    public List<Player> getPlayers() {
        return playerRepository.findAll();
    }
}
