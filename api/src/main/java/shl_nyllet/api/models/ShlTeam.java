package shl_nyllet.api.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class ShlTeam {
	@Id
	String uuid;
	String teamCode;
}
