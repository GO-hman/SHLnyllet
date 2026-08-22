package shl_nyllet.api.shlintegration;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import shl_nyllet.api.shlintegration.dto.ShlPlayer;
import shl_nyllet.api.shlintegration.dto.ShlPositionGroup;
import shl_nyllet.api.shlintegration.dto.ShlSiteSettings;
import shl_nyllet.api.shlintegration.dto.ShlTeam;

import java.util.List;

@Component
public class ShlDataClient {

	private final RestClient restClient;

	public ShlDataClient(RestClient restClient) {
		this.restClient = restClient;
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

		return allPlayers;
	}

	public List<ShlTeam> fetchTeams() {
		ShlSiteSettings siteSettings = restClient.get()
				.uri("/site/settings")
				.retrieve()
				.body(ShlSiteSettings.class);
		return siteSettings.getTeamsInSite();
	}

	public ShlPlayer fetchPlayerById(String id) {
		ShlPlayer player = restClient.get()
				.uri("/statistics-v2/athlete/profile-page?playerUuid={id}", id)
				.retrieve()
				.body(ShlPlayer.class);
		return player;
	}
}
