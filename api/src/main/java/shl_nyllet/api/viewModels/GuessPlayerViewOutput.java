package shl_nyllet.api.viewModels;

import lombok.Data;
import shl_nyllet.api.models.ShlPlayer;

@Data
public class GuessPlayerViewOutput {
    String uuid;
    String imgUrl;

    public GuessPlayerViewOutput(ShlPlayer player) {
        this.uuid = player.getUuid();
        this.imgUrl = player.getRenderedLatestPortrait().getUrl();
    }
}
