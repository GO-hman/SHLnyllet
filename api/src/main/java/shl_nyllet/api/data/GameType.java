package shl_nyllet.api.data;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(enumAsRef = true)
public enum GameType {
    GUESS_NAME,
    GUESS_NUMBER
}
