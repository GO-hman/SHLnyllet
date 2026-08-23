package shl_nyllet.api.shlintegration;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import shl_nyllet.api.models.ShlPlayer;
import shl_nyllet.api.models.ShlPositionGroup;
import shl_nyllet.api.models.ShlSiteSettings;
import shl_nyllet.api.models.ShlTeam;
import shl_nyllet.api.models.ShlTeamCode;

@Component
public class ShlApiClient {

	private final RestClient restClient;

	public ShlApiClient(RestClient restClient) {
		this.restClient = restClient;
	}

	public List<ShlPlayer> fetchPlayersByTeam(String teamId) {
		List<ShlPositionGroup> positionGroups = restClient.get()
				.uri("/sports-v2/athletes/by-team-uuid/{id}", teamId)
				.retrieve()
				.body(new ParameterizedTypeReference<>() {
				});

		return positionGroups.stream()
				.flatMap(group -> group.getPlayers().stream())
				.toList();
	}

	public List<ShlTeam> fetchTeams() {
		ShlSiteSettings siteSettings = restClient.get()
				.uri("/site/settings")
				.header("x-s8y-instance-id", "shl1_shl")
				.retrieve()
				.body(ShlSiteSettings.class);

		return siteSettings.getAllTeamsInSite().stream()
				.filter(t -> isShlTeam(t.getTeamCode()))
				.toList();
	}

	public ShlPlayer fetchPlayerById(String id) {
		return restClient.get()
				.uri("/statistics-v2/athlete/profile-page?playerUuid={id}", id)
				.retrieve()
				.body(ShlPlayer.class);
	}

	private boolean isShlTeam(String teamCode) {
		try {
			ShlTeamCode.valueOf(teamCode);
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}
}
