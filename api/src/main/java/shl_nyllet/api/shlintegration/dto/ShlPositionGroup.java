package shl_nyllet.api.shlintegration.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShlPositionGroup {
        String position;
        String positionCode;
        List<ShlPlayer> players;
}
