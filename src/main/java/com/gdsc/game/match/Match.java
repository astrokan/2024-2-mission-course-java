package com.gdsc.game.match;


import com.gdsc.game.player.Player;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@NoArgsConstructor
@Getter
public class Match {

    private final int MAX_TURN = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "match_id")
    private Long id;

    @Column(name = "current_turn")
    private int currentTurn;

    @Column(name = "max_turn", insertable = false, updatable = false)
    private int maxTurn;

    @OneToOne // Match에 Player 1 연결
    @JoinColumn(name = "player1_name", nullable = false)
    private Player player1;

    @OneToOne // Match에 Player 2 연결
    @JoinColumn(name = "player2_name", nullable = false)
    private Player player2;

    @OneToOne
    @JoinColumn(name = "current_player_name")
    private Player currentPlayer;

    @ElementCollection
    @CollectionTable(name = "action_result_map", joinColumns = @JoinColumn(name = "match_id"))
    @MapKeyColumn(name = "player_name") // Map의 키
    @Column(name = "damage_value") // Map의 값
    private Map<String, Integer> damageMap = new HashMap<>();


    public Match(List<Player> players) {
        currentTurn = 0;
        maxTurn = MAX_TURN;

        player1 = players.get(0);
        player2 = players.get(1);
        currentPlayer = player1;
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
        return players;
    }

    public void nextTurn() {
        currentTurn++;
    }

    public boolean isFinished() {
        return currentTurn >= maxTurn || !player1.isAlive() || !player2.isAlive();
    }

    public int getMaxTurn() {
        return maxTurn;
    }
}
