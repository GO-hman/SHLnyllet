package shl_nyllet.api.shlintegration;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import shl_nyllet.api.shlintegration.dto.ShlPlayerDto;
import shl_nyllet.api.shlintegration.dto.ShlPositionGroupDto;

import java.util.List;

@Component
public class ShlDataClient {

    private final RestClient restClient;

    public ShlDataClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void fetchItems() {
        List<ShlPositionGroupDto> positionGroups = restClient.get()
                .uri("/athletes/by-team-uuid/4519-4519Rdei6")
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        List<ShlPlayerDto> goalkeepers = positionGroups.stream()
                .filter(group -> "GK".equals(group.positionCode()))
                .flatMap(group -> group.players().stream())
                .toList();

        List<ShlPlayerDto> defenders = positionGroups.stream()
                .filter(group -> "D".equals(group.positionCode()))
                .flatMap(group -> group.players().stream())
                .toList();

        List<ShlPlayerDto> forwards = positionGroups.stream()
                .filter(group -> "F".equals(group.positionCode()))
                .flatMap(group -> group.players().stream())
                .toList();

        System.out.println(goalkeepers);
        System.out.println(defenders);
        System.out.println(forwards);
    }
}
