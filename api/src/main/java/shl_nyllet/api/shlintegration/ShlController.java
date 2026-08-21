package shl_nyllet.api.shlintegration;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class ShlController {

    private final ShlDataClient shlClient;

    public ShlController(ShlDataClient shlClient) {
        this.shlClient = shlClient;
    }

    @GetMapping("/shl/test")
    public String test() {
        shlClient.fetchItems();
        return "ok";
    }
}
