package com.gdsc.game.match;

import com.gdsc.game.action.Action;
import com.gdsc.game.action.skill.Skill;
import com.gdsc.game.player.Player;
import com.gdsc.game.player.PlayerRepository;
import com.gdsc.game.player.PlayerService;
import com.gdsc.game.player.skillcooldown.SkillCooldown;
import com.gdsc.game.player.skillcooldown.SkillCooldownRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

import static java.lang.Math.abs;


@Service
@RequiredArgsConstructor
public class MatchService {

    private final PlayerService playerService;
    private final MatchRepository matchRepository;
    private final PlayerRepository playerRepository;
    private final SkillCooldownRepository skillCooldownRepository;


    // 게임 시작 세팅
    @Transactional
    public Long startGame() {
        List<Player> players = playerService.getPlayers();

        if (players.size() != 2) {
            throw new RuntimeException("플레이어가 2명이 아닙니다. 게임 세팅을 취소합니다.");
        }
        if (matchExists()) {
            throw new RuntimeException("게임이 진행 중입니다. 게임 세팅을 취소합니다.");
        }

        Match match = new Match(players);
        matchRepository.save(match);
        return match.getId();
    }

    private boolean matchExists() {
        List<Match> matches = matchRepository.findAll();
        return matches.size() == 1;
    }

    // 플레이어에게 행동을 입력받음
    @Transactional
    public String handlePlayerAction(String playerName, int actionIdx, Long matchId) {
        List<Player> players = playerService.getPlayers();
        Match match = matchRepository.findById(matchId);
        Player player = playerRepository.findByName(playerName);

        // match 끝남
        if (match == null) {
            throw new RuntimeException("존재하지 않는 경기입니다.");
        }
        // 플레이어가 현재 차례가 아님
        if (player != match.getCurrentPlayer()) {
            throw new RuntimeException("상대의 턴 완료를 기다리십시오!");
        }

        if (players.equals(match.getPlayer1())) { // player1이었다면, 행동 저장
            saveAction(player, actionIdx-1, matchId);
            return "행동 선택 완료! 다른 플레이어의 턴 수행을 기다립니다.";
        }
        else {  // player2였다면, 행동 저장 + 한 턴 종료 후 결과 처리
            saveAction(player, actionIdx-1, matchId);
            return showTurnResult(match);
        }
    }

    // 한 명의 플레이어의 행동 입력을 damageMap에 저장
    private void saveAction(Player player, int actionIdx, Long matchId) {
        Match match = matchRepository.findById(matchId);
        Map<String, Integer> damageMap = match.getDamageMap();

        // 플레이어의 행동 선택 & 공격/방어 데미지 계산
        Action action = chooseAction(player, actionIdx);
        int damage = action.execute();
        damageMap.put(player.getName(), damage);
    }

    // 스킬을 특수 처리(쿨타임, mp 고려)하기 위한 메서드이다. 일반 공격, 일반 방어는 추가 처리 없이 리턴한다.
    private Action chooseAction(Player player, int actionIdx) throws RuntimeException {

        List<Action> actionList = playerService.createActionList(player);
        Action chosenAction = actionList.get(actionIdx);

        if (chosenAction instanceof Skill) {    // 스킬의 경우
            Skill skill = (Skill) chosenAction;
            SkillCooldown skillCooldown = skillCooldownRepository.findOneByPlayerAndSkill(player, skill);
            int requiredMp = skill.getMpCost();  // 스킬의 소비 MP

            if (!skillCooldown.canUseSkill()) {
                throw new RuntimeException("쿨타임이 남았습니다. 다시 선택하세요.");
            }
            else if (player.getMp() < requiredMp) {
                throw new RuntimeException("MP가 부족합니다. 다시 선택하세요.");
            }
            else {
                skillCooldown.startSkillCooldown(); // 스킬 쿨타임 카운트 시작
                player.consumeMp(requiredMp); // mp 차감
                return chosenAction;
            }
        } else {
            return chosenAction; // 스킬이 아닌 일반 액션
        }
    }

    // 한 턴이 끝난 상태. 저장했던 두 플레이어 행동을 반영할 차례.
    private String showTurnResult(Match match) {
        Player player1 = match.getPlayer1();
        Player player2 = match.getPlayer2();

        Map<String, Integer> damageMap = match.getDamageMap();
        int player1Damage = damageMap.get(player1.getName());
        int player2Damage = damageMap.get(player2.getName());

        processResults(player1, player1Damage, player2, player2Damage, match); // 행동 반영
        endTurn(match); // 턴 종료(쿨타임 차감, 다음 턴으로 갱신)
        if (match.isFinished()) {
            return printMatchResult(match);
        }
        return "턴이 종료되었습니다! 다음 턴을 진행합니다.|| 최근 완료된 턴: " + (match.getCurrentTurn()-1) +  "마지막 턴 : " + match.getMaxTurn();
    }

    // 쿨타임 차감, 다음 턴으로 갱신
    private void endTurn(Match match) {
        List<Player> players = match.getPlayers();
        for (Player player : players) {
            List<SkillCooldown> cooldowns = skillCooldownRepository.findSkillCooldownByPlayer(player);
            for (SkillCooldown cooldown : cooldowns) {
                cooldown.updateRemainingCooldown();
            }
        }
        match.nextTurn(); // 다음 턴으로 넘어감.
    }

    private void processResults(Player player1, int damage1, Player player2, int damage2, Match match) {
        Map<String, Integer> damageMap = match.getDamageMap();

        // 플레이어 1과 2의 선택을 각각 저장
        boolean player1Attacking = damage1 >= 0; // 양수면 공격 데미지, 음수면 방어 데미지
        boolean player2Attacking = damage2 >= 0;
        int damageToPlayer1; // 플레이어2가 플레이어1에게 주는 데미지
        int damageToPlayer2; // 플레이어1이 플레이어2에게 주는 데미지

        if (player1Attacking) {
            if (player2Attacking) { // 플레이어 1: 공격, 플레이어 2: 공격
                damageToPlayer1 = damage2;
                damageToPlayer2 = damage1;
                System.out.println("플레이어 1: 공격, 플레이어 2: 공격");
            }
            else {  // 플레이어 1: 공격, 플레이어 2: 방어
                damageToPlayer1 = 0;
                damageToPlayer2 = Math.max(0, damage1 - abs(damage2)); // 공격 데미지 - 방어 데미지
                System.out.println("플레이어 1: 공격, 플레이어 2: 방어");
            }
        }
        else {
            if (player2Attacking) { // 플레이어 1: 방어, 플레이어 2: 공격
                damageToPlayer1 = Math.max(0, damage2 - abs(damage1)); // 공격 데미지 - 방어 데미지
                damageToPlayer2 = 0;
                System.out.println("플레이어 1: 방어, 플레이어 2: 공격");
            }
            else { // 플레이어 1: 방어, 플레이어 2: 방어
                damageToPlayer1 = 0;
                damageToPlayer2 = 0;
                System.out.println("플레이어 1: 방어, 플레이어 2: 방어");
            }
        }
        // 데미지 적용
        player1.applyDamage(damageToPlayer1);
        player2.applyDamage(damageToPlayer2);
        damageMap.clear(); // 두 플레이어의 행동을 저장했던 data 삭제
    }

    // 게임 결과를 요청받으면 결과 메시지를 리턴
    private String printMatchResult(Match match) {
        Player player1 = match.getPlayer1();
        Player player2 = match.getPlayer2();

        if (!player1.isAlive() && !player2.isAlive()) { // 둘 다 hp 소진
            return "두 플레이어가 모두 쓰려졌습니다....무승부!";
        }
        else if (!player1.isAlive()) {  // player1 hp 소진
            return player2.getName() + "이(가) 이겼습니다!";
        } else if (!player2.isAlive()) {    // player2 hp 소진
            return player1.getName() + "이(가) 이겼습니다!";
        }
        else {
            return "게임 진행 중입니다!";
        }
    }

    @Transactional
    public void deleteMatch(Long matchId) throws RuntimeException {
        Match match = matchRepository.findById(matchId);

        if (match == null) {
            throw new RuntimeException("Match not found");
        }
        List<Player> players = match.getPlayers();
        for (Player player : players) {
            List<SkillCooldown> cooldowns = skillCooldownRepository.findSkillCooldownByPlayer(player);
            for (SkillCooldown cooldown : cooldowns) {
                cooldown.initializeCooldown(); // 스킬 쿨타임 0으로 초기화
            }
        }
        match.getDamageMap().clear(); // 턴마다 플레이어 행동을 저장하는 Map 내용 삭제
        matchRepository.delete(match); // match 삭제
    }
 }
