package my.demo.tradingview.config;

import java.util.Collections;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
public class WebConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    CorsConfiguration corsConfig = new CorsConfiguration();
    corsConfig.setAllowedMethods(Collections.singletonList("*"));
    corsConfig.setAllowedHeaders(Collections.singletonList("*"));
    corsConfig.setMaxAge(3600L);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfig);

    http.httpBasic()
        .and().authorizeRequests().anyRequest().permitAll()
        .and().cors().disable()
          .csrf().disable();
  }

}
