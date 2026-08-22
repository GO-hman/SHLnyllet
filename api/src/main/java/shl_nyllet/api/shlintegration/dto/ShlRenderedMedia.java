package shl_nyllet.api.shlintegration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShlRenderedMedia {
    String url;
}
