package shl_nyllet.api.shlintegration;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import shl_nyllet.api.shlintegration.dto.ShlPlayer;
import shl_nyllet.api.shlintegration.dto.ShlTeam;

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
}
