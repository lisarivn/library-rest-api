package library;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import library.security.JwtRequestFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Off CSRF, because we use JWT
            .csrf(csrf -> csrf.disable())
            
            // Configure access permissions
            .authorizeHttpRequests(auth -> auth
                // Public
                .requestMatchers("/", "/*.html", "/*.js", "/api/v1/auth/login", "/css/**", "/error").permitAll()
                
                // Enable GET for all authorized users
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/literature/**").hasAnyRole("USER", "ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/v1/**").hasAnyRole("USER", "ADMIN")
                
                // ONLY THE ADMIN CAN MODIFY THE DATA (POST, PUT, DELETE)
                .requestMatchers("/api/v1/**").hasRole("ADMIN") 
                
                .anyRequest().authenticated()
            )
            
            // Off session
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}