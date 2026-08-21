package shl_nyllet.api.shlintegration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShlPlayerDto(
    String uuid,
    String firstName,
    String lastName,
    String fullName,
    String nationality,
    int jerseyNumber,
    ShlRenderedMediaDto renderedLatestPortrait
) {}
