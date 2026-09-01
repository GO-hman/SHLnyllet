package shl_nyllet.api.services;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.stereotype.Component;

import shl_nyllet.api.data.ShlPlayerRepository;
import shl_nyllet.api.models.ShlPlayer;
import shl_nyllet.api.viewModels.PlayerNameViewOutput;

@Component
public class GuessPlayerService {

    private final ShlPlayerRepository playerRepo;

    public GuessPlayerService(ShlPlayerRepository playerRepo) {
        this.playerRepo = playerRepo;
    }

    public Optional<ShlPlayer> guessPlayer(String id, String name) {
        ShlPlayer player = playerRepo.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Player not found: " + id));

        if (player.getFullName().equalsIgnoreCase(name)) {
            return Optional.of(player);
        }
        return Optional.empty();
    }

    public ShlPlayer getRandomPlayer() {
        return playerRepo.findRandomPlayer();
    }

    public ShlPlayer getRandomPlayerByTeam(String id) {
        var player = playerRepo.findRandomPlayerByTeamUuid(id);
        return player;
    }

    public List<PlayerNameViewOutput> getPlayerNames() {
        return playerRepo.findAll().stream()
                .map(PlayerNameViewOutput::new).toList();
    }
}
