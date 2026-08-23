package shl_nyllet.api.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class ShlPlayer {
    @Id
    String uuid;
    String firstName;
    String lastName;
    String fullName;
    String nationality;
    int jerseyNumber;
    @JsonAlias("media")
    @Embedded
    ShlRenderedMedia renderedLatestPortrait;
}
