package jp.co.axa.apidemo.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        .csrf().disable()
        .authorizeRequests()
        // Allowing all swagger and health endpoints
        .antMatchers("/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/actuator/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .httpBasic();
    return http.build();
  }

  @SuppressWarnings("java:S6437") // Authentication for demo purposes only
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth,
                              PasswordEncoder passwordEncoder) throws Exception {
    auth.inMemoryAuthentication()
        .withUser("user")
        .password(passwordEncoder.encode("password"))
        .roles("USER");
  }
}
