package shl_nyllet.api.services;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import shl_nyllet.api.data.ShlPlayerRepository;
import shl_nyllet.api.data.ShlTeamRepository;
import shl_nyllet.api.models.ShlPlayer;
import shl_nyllet.api.models.ShlTeam;
import shl_nyllet.api.shlintegration.ShlApiClient;

@Component
public class ShlSyncService {

	private final ShlApiClient apiClient;
	private final ShlTeamRepository teamRepo;
	private final ShlPlayerRepository playerRepo;

	public ShlSyncService(ShlApiClient apiClient, ShlTeamRepository teamRepo, ShlPlayerRepository playerRepo) {
		this.apiClient = apiClient;
		this.teamRepo = teamRepo;
		this.playerRepo = playerRepo;
	}

	public List<ShlTeam> syncTeams() {
		List<ShlTeam> teams = apiClient.fetchTeams();
		Set<String> existingIds = teamRepo.findAllUuids();

		List<ShlTeam> newTeams = teams.stream()
				.filter(team -> !existingIds.contains(team.getUuid()))
				.toList();
		if (!newTeams.isEmpty()) {
			teamRepo.saveAll(newTeams);
		}
		return teams;
	}

	public List<ShlPlayer> syncPlayersForTeam(String teamId) {
		List<ShlPlayer> players = apiClient.fetchPlayersByTeam(teamId);
		playerRepo.saveAll(players);
		return players;
	}

	public void syncAllPlayers() {
		teamRepo.findAll().parallelStream()
				.forEach(team -> syncPlayersForTeam(team.getUuid()));
	}
}
