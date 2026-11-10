package dev.cobaltgate.security;
import org.springframework.context.annotation.*;
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
  var encoder=new BCryptPasswordEncoder();
  var user=User.withUsername(env.getProperty("DEMO_ADMIN_USER","portfolio"))
    .password("{bcrypt}" + encoder.encode(env.getProperty("DEMO_ADMIN_PASSWORD","ChangeMeLocal")))
    .roles("ADMIN").build();
  return new InMemoryUserDetailsManager(user);
 }
 @Bean SecurityFilterChain chain(HttpSecurity http) throws Exception {
  return http.csrf(csrf->csrf.disable()) // non-browser API using HTTP Basic in development
    .authorizeHttpRequests(auth->auth.requestMatchers("/actuator/health").permitAll().anyRequest().hasRole("ADMIN"))
    .httpBasic(Customizer.withDefaults()).build();
 }
}
