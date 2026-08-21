package shl_nyllet.api.shlintegration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShlPositionGroupDto(
    String position,
    String positionCode,
    List<ShlPlayerDto> players
) {}
