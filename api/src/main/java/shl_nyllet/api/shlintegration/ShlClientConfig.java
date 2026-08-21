package shl_nyllet.api.shlintegration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class ShlClientConfig {

    @Bean
    RestClient externalSHLClient() {
        return RestClient.builder()
                .baseUrl("https://www.shl.se/api/sports-v2").build();
    }

}
