package shl_nyllet.api.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShlSiteSettings {
	List<ShlTeam> allTeamsInSite;
}
