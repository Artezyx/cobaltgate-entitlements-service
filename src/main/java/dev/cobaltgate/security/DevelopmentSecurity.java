package dev.cobaltgate.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class DevelopmentSecurity {
 @Bean UserDetailsService users(Environment env) {
  var encoder = new BCryptPasswordEncoder();
  var admin = User.withUsername(env.getProperty("DEMO_ADMIN_USER","portfolio"))
    .password("{bcrypt}" + encoder.encode(env.getProperty("DEMO_ADMIN_PASSWORD","ChangeMeLocal")))
    .roles("ADMIN", "READER").build();
  var reader = User.withUsername(env.getProperty("DEMO_READER_USER","read-only"))
    .password("{bcrypt}" + encoder.encode(env.getProperty("DEMO_READER_PASSWORD","ChangeMeReaderLocal")))
    .roles("READER").build();
  return new InMemoryUserDetailsManager(admin, reader);
 }
 @Bean SecurityFilterChain chain(HttpSecurity http) throws Exception {
  return http.csrf(csrf -> csrf.disable()) // stateless non-browser HTTP Basic API only
    .authorizeHttpRequests(auth -> auth
      .requestMatchers("/actuator/health").permitAll()
      .requestMatchers(HttpMethod.GET, "/api/v1/subscriptions/**").hasAnyRole("READER","ADMIN")
      .requestMatchers("/api/v1/subscriptions/**").hasRole("ADMIN")
      .anyRequest().denyAll())
    .httpBasic(Customizer.withDefaults()).build();
 }
}
