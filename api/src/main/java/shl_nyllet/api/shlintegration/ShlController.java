package shl_nyllet.api.shlintegration;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import shl_nyllet.api.models.ShlPlayer;
import shl_nyllet.api.models.ShlTeam;
import shl_nyllet.api.viewModels.GuessPlayer;

@RestController()
public class ShlController {

    private final ShlDataClient shlClient;

    public ShlController(ShlDataClient shlClient) {
        this.shlClient = shlClient;
    }

    @GetMapping("/shl/teams/{id}/players")
    public List<ShlPlayer> fetchByTeam(@PathVariable String id) {
        return shlClient.fetchPlayersByTeam(id);
    }

    @GetMapping("/shl/teams")
    public List<ShlTeam> allTeams() {
        var teams = shlClient.fetchTeams();
        return teams;
    }

    @GetMapping("/shl/player/{id}")
    public ShlPlayer getPlayer(@PathVariable String id) {
        ShlPlayer player = shlClient.fetchPlayerById(id);
        return player;
    }

    @GetMapping("shl/player/save")
    public void savePlayers() {
        shlClient.saveAllPlayersToDb();
    }

    @PostMapping("/shl/player/guess")
    public ResponseEntity<ShlPlayer> guessPlayer(@RequestBody GuessPlayer guess) {
        return shlClient.guessPlayer(guess.getId(), guess.getName())
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

}
