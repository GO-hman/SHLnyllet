package shl_nyllet.api.shlintegration;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import shl_nyllet.api.data.GameType;
import shl_nyllet.api.data.ShlTeamRepository;
import shl_nyllet.api.models.ShlPlayer;
import shl_nyllet.api.models.ShlTeam;
import shl_nyllet.api.services.GuessPlayerService;
import shl_nyllet.api.services.ShlSyncService;
import shl_nyllet.api.viewModels.GuessNameViewInput;
import shl_nyllet.api.viewModels.GuessNumberViewInput;
import shl_nyllet.api.viewModels.GuessPlayerViewOutput;
import shl_nyllet.api.viewModels.PlayerNameViewOutput;

@RequestMapping("/shl")
@RestController()
public class ShlController {

    private final ShlApiClient shlApiClient;
    private final ShlSyncService shlSyncService;
    private final GuessPlayerService playerGuessService;
    private final ShlTeamRepository teamRepo;

    public ShlController(ShlApiClient shlApiClient, ShlSyncService shlSyncService,
            GuessPlayerService playerGuessService, ShlTeamRepository teamRepo) {
        this.shlApiClient = shlApiClient;
        this.shlSyncService = shlSyncService;
        this.playerGuessService = playerGuessService;
        this.teamRepo = teamRepo;
    }

    @GetMapping("/teams/{id}/players")
    public List<ShlPlayer> fetchByTeam(@PathVariable String id) {
        return shlApiClient.fetchPlayersByTeam(id);
    }

    @GetMapping("/teams")
    public List<ShlTeam> allTeams() {
        return teamRepo.findAll();
    }

    @GetMapping("/player/{id}")
    public ShlPlayer getPlayer(@PathVariable String id) {
        return shlApiClient.fetchPlayerById(id);
    }

    @PostMapping("/teams/sync")
    public List<ShlTeam> syncTeams() {
        return shlSyncService.syncTeams();
    }

    @PostMapping("/players/sync")
    public void syncPlayers() {
        shlSyncService.syncAllPlayers();
    }

    @GetMapping("/player/random")
    public GuessPlayerViewOutput randomPlayer(@RequestParam(defaultValue = "GUESS_NAME") GameType game) {
        return new GuessPlayerViewOutput(playerGuessService.getRandomPlayer(), game);
    }

    @GetMapping("/player/{teamId}/random")
    public GuessPlayerViewOutput randomPlayerFromTeam(
            @PathVariable String teamId,
            @RequestParam(defaultValue = "GUESS_NAME") GameType game) {
        return new GuessPlayerViewOutput(playerGuessService.getRandomPlayerByTeam(teamId), game);
    }

    @GetMapping("/player/playernames")
    public List<PlayerNameViewOutput> playerNames() {
        return playerGuessService.getPlayerNames();
    }

    @PostMapping("/player/guessName")
    public ResponseEntity<ShlPlayer> guessName(@RequestBody GuessNameViewInput guess) {
        return playerGuessService.guessName(guess.getId(), guess.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/player/guessNumber")
    public ResponseEntity<ShlPlayer> guessNumber(@RequestBody GuessNumberViewInput guess) {
        return playerGuessService.guessNumber(guess.getId(), guess.getJerseyNumber())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

}
