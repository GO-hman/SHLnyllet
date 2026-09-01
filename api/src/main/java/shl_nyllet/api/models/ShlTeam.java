package shl_nyllet.api.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class ShlTeam {
	@Id
	String uuid;
	@JsonAlias("code")
	String teamCode;
	@JsonIgnore
	@OneToMany(mappedBy = "team")
	List<ShlPlayer> players;
}
