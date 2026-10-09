package shl_nyllet.api.security;

import java.beans.BeanProperty;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Value("${CORS_ALLOWED_ORIGIN:http://localhost:4200}")
	private String corsAllowedOrigin;

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(List.of(corsAllowedOrigin));
		config.setAllowedMethods(List.of("*"));
		config.setAllowedHeaders(List.of("*"));
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationService authService) throws Exception {
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()))

				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(
						auth -> auth
								.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
								.requestMatchers("/**").authenticated())
				.httpBasic(Customizer.withDefaults())
				.sessionManagement(
						httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
								.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(new AuthenticationFilter(authService),
						UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
