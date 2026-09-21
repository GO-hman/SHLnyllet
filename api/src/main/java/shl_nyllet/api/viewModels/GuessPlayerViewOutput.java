package shl_nyllet.api.viewModels;

import shl_nyllet.api.data.GameType;
import shl_nyllet.api.models.ShlPlayer;

public class GuessPlayerViewOutput {
    private String uuid;
    private String imgUrl;
    private String name;
    private Integer jerseryNumber;

    public GuessPlayerViewOutput(ShlPlayer player, GameType game) {
        this.uuid = player.getUuid();
        this.imgUrl = player.getRenderedLatestPortrait().getUrl();
        this.name = game == GameType.GUESS_NAME ? null : player.getFullName();
        this.jerseryNumber = player.getJerseyNumber();
    }

    public String getUuid() {
        return this.uuid;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getName() {
        return this.name;
    }

    public Integer getJerserNumber() {
        return this.jerseryNumber;
    }
}
