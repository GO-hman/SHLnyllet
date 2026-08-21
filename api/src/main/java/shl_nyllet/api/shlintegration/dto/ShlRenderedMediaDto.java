package shl_nyllet.api.shlintegration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ShlRenderedMediaDto(
    String url
) {}
