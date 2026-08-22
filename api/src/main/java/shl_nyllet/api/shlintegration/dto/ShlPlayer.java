package shl_nyllet.api.shlintegration.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShlPlayer {
    String uuid;
    String firstName;
    String lastName;
    String fullName;
    String nationality;
    int jerseyNumber;
    @JsonAlias("media")
    ShlRenderedMedia renderedLatestPortrait;
}
