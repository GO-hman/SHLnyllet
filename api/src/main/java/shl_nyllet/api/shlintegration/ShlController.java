package shl_nyllet.api.shlintegration;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import shl_nyllet.api.models.ShlPlayer;
import shl_nyllet.api.models.ShlTeam;
import shl_nyllet.api.services.GuessPlayerService;
import shl_nyllet.api.services.ShlSyncService;
import shl_nyllet.api.viewModels.GuessPlayerViewInput;
import shl_nyllet.api.viewModels.GuessPlayerViewOutput;

@RestController()
public class ShlController {

    private final ShlApiClient shlApiClient;
    private final ShlSyncService shlSyncService;
    private final GuessPlayerService playerGuessService;

    public ShlController(ShlApiClient shlApiClient, ShlSyncService shlSyncService,
            GuessPlayerService playerGuessService) {
        this.shlApiClient = shlApiClient;
        this.shlSyncService = shlSyncService;
        this.playerGuessService = playerGuessService;
    }

    @GetMapping("/shl/teams/{id}/players")
    public List<ShlPlayer> fetchByTeam(@PathVariable String id) {
        return shlApiClient.fetchPlayersByTeam(id);
    }

    @GetMapping("/shl/teams")
    public List<ShlTeam> allTeams() {
        return shlApiClient.fetchTeams();
    }

    @GetMapping("/shl/player/{id}")
    public ShlPlayer getPlayer(@PathVariable String id) {
        return shlApiClient.fetchPlayerById(id);
    }

    @PostMapping("/shl/teams/sync")
    public List<ShlTeam> syncTeams() {
        return shlSyncService.syncTeams();
    }

    @PostMapping("/shl/players/sync")
    public void syncPlayers() {
        shlSyncService.syncAllPlayers();
    }

    @PostMapping("/shl/player/guess")
    public ResponseEntity<ShlPlayer> guessPlayer(@RequestBody GuessPlayerViewInput guess) {
        return playerGuessService.guessPlayer(guess.getId(), guess.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @GetMapping("/shl/player/random")
    public GuessPlayerViewOutput randomPlayer() {
        return new GuessPlayerViewOutput(playerGuessService.getRandomPlayer());
    }
}
