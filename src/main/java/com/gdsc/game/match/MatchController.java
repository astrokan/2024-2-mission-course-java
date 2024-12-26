package com.gdsc.game.match;

import com.gdsc.game.common.exception.JobNotFoundException;
import com.gdsc.game.match.dto.PlayerCreateRequest;
import com.gdsc.game.player.PlayerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequiredArgsConstructor
@RequestMapping("/game")
public class MatchController {

    private final PlayerService playerService;
    private final MatchService matchService;

    @PostMapping("/register")
    public ResponseEntity<Void> createPlayer(@RequestBody @Valid PlayerCreateRequest request) {
        try {
            String playerName = playerService.createPlayer(request.getPlayerName(), request.getJobName(), request.getLevel());
            return ResponseEntity.created(URI.create("game/"+playerName)).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null); // 400 Bad Request
        } catch (JobNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null); // 404 Not Found
        }
    }

    @PostMapping("/start")
    public ResponseEntity<Void> createMatch() {
        Long matchId = matchService.startGame();
        return ResponseEntity.created(URI.create("game/"+matchId)).build();
    }

    @GetMapping("/status") // 두 플레이어 상태 확인
    public ResponseEntity<String> getStatus() {
        String response = playerService.printPlayersState();
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{playerName}/action") // 특정 플레이어의 액션 리스트 뽑아냄.
    public ResponseEntity<String> getActionList(@PathVariable String playerName) {
        String response = playerService.printActionList(playerName);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/{playerName}/{actionIdx}") // 특정 플레이어가 특정 액션을 요청 바디에 넣음(게임의 중추)
    public ResponseEntity<String> chooseAction(@PathVariable String playerName, @PathVariable int actionIdx) {
        String response = matchService.handlePlayerAction(playerName, actionIdx, 1L); // db에서 하나의 경기만을 다룰 것임.
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/reset")
    public ResponseEntity<Void> deleteMatch(){
        matchService.deleteMatch(1L);
        return ResponseEntity.noContent().build(); // 204 no content
    }

}