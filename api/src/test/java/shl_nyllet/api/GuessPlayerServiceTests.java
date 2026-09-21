package shl_nyllet.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import shl_nyllet.api.data.ShlPlayerRepository;
import shl_nyllet.api.models.ShlPlayer;
import shl_nyllet.api.models.ShlTeam;
import shl_nyllet.api.services.GuessPlayerService;

@ExtendWith(MockitoExtension.class)
public class GuessPlayerServiceTests {

    @Mock
    ShlPlayerRepository playerRepo;

    @InjectMocks
    GuessPlayerService playerService;

    private ShlTeam createTeam() {
        ShlTeam shlTeam = new ShlTeam();
        shlTeam.setUuid("abcd123ABCD!#");
        shlTeam.setTeamCode("TEAM");
        return shlTeam;
    }

    private ShlPlayer createPlayer() {
        ShlPlayer player = new ShlPlayer();
        player.setFullName("Jan Sandström");
        player.setJerseyNumber(7);
        player.setTeam(null);
        return player;
    }

    private void givenRandomPlayerForTeam(ShlPlayer player) {
        when(playerRepo.findRandomPlayerByTeamUuid(player.getTeam().getUuid()))
                .thenReturn(player);
    }

    @Test
    void getRandomPlayer_returnsPlayerFromRepo() {
        // Arrange
        ShlPlayer player = createPlayer();
        when(playerRepo.findRandomPlayer()).thenReturn(player);

        // Act
        ShlPlayer result = playerService.getRandomPlayer();

        // Assert
        assertEquals(player, result);
    }

    @Test
    void getRandomPlayerFromTeam_returnsPlayerFromGivenTeam() {
        // Arrange
        ShlTeam team = createTeam();
        ShlPlayer player = createPlayer();
        player.setTeam(team);
        givenRandomPlayerForTeam(player);

        // Act

        ShlPlayer result = playerService.getRandomPlayerByTeam(team.getUuid());

        // Assert

        assertEquals(player, result);
        assertSame(team, player.getTeam());

    }
}
