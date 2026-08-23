package shl_nyllet.api.shlintegration;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import shl_nyllet.api.data.ShlPlayerRepository;
import shl_nyllet.api.data.ShlTeamRepository;
import shl_nyllet.api.models.ShlPlayer;
import shl_nyllet.api.models.ShlPositionGroup;
import shl_nyllet.api.models.ShlSiteSettings;
import shl_nyllet.api.models.ShlTeam;
import shl_nyllet.api.models.ShlTeamCode;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

@Component
public class ShlDataClient {

	private final RestClient restClient;
	private final ShlTeamRepository teamRepo;
	private final ShlPlayerRepository playerRepo;

	public ShlDataClient(RestClient restClient, ShlTeamRepository teamRepo, ShlPlayerRepository playerRepo) {
		this.restClient = restClient;
		this.teamRepo = teamRepo;
		this.playerRepo = playerRepo;
	}

	public List<ShlPlayer> fetchPlayersByTeam(String id) {
		List<ShlPositionGroup> positionGroups = restClient.get()
				.uri("/sports-v2/athletes/by-team-uuid/{id}", id)
				.retrieve()
				.body(new ParameterizedTypeReference<>() {
				});

		List<ShlPlayer> allPlayers = positionGroups.stream()
				.flatMap(group -> group.getPlayers().stream())
				.toList();
		savePlayersToDb(allPlayers);

		return allPlayers;
	}

	public void saveAllPlayersToDb() {
		List<ShlTeam> allTeams = teamRepo.findAll();
		for (ShlTeam team : allTeams) {
			fetchPlayersByTeam(team.getUuid());
		}
	}

	public List<ShlTeam> fetchTeams() {
		ShlSiteSettings siteSettings = restClient.get()
				.uri("/site/settings")
				.header("x-s8y-instance-id", "shl1_shl")
				.retrieve()
				.body(ShlSiteSettings.class);
		List<ShlTeam> teams = siteSettings.getAllTeamsInSite().stream()
				.filter(t -> isShlTeam(t.getTeamCode()))
				.toList();
		saveTeamsToDb(teams);
		return teams;
	}

	public void savePlayersToDb(List<ShlPlayer> players) {
		playerRepo.saveAll(players);
	}

	public void saveTeamsToDb(List<ShlTeam> teams) {
		Set<String> existingIds = teamRepo.findAllUuids();
		System.out.println(existingIds.size());

		List<ShlTeam> newTeams = teams.stream()
				.filter(team -> !existingIds.contains(team.getUuid()))
				.toList();
		if (!newTeams.isEmpty()) {
			System.out.println(newTeams.size());
			teamRepo.saveAll(newTeams);

		}
	}

	private boolean isShlTeam(String teamCode) {
		try {
			ShlTeamCode.valueOf(teamCode);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	public ShlPlayer fetchPlayerById(String id) {
		ShlPlayer player = restClient.get()
				.uri("/statistics-v2/athlete/profile-page?playerUuid={id}", id)
				.retrieve()
				.body(ShlPlayer.class);
		return player;
	}

	public Optional<ShlPlayer> guessPlayer(String id, String name) {
		ShlPlayer player = playerRepo.findById(id)
				.orElseThrow(() -> new NoSuchElementException("Player not found: " + id));

		if (player.getFullName().equalsIgnoreCase(name)) {
			return Optional.of(player);
		}
		return Optional.empty();
	}

}
