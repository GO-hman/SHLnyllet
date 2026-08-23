package shl_nyllet.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Embeddable
public class ShlRenderedMedia {
    String url;
}
