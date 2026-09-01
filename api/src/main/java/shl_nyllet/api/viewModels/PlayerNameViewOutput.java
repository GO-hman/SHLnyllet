package shl_nyllet.api.viewModels;

import lombok.Data;
import shl_nyllet.api.models.ShlPlayer;

@Data
public class PlayerNameViewOutput {

    String name;

    public PlayerNameViewOutput(ShlPlayer player) {
        this.name = player.getFullName();
    }
}
