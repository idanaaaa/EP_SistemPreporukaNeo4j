package ptf.si.ep.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String ADMIN = "ADMIN";

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // REST API: HTTP Basic pri svakom zahtjevu, bez sesije. Bez sesijskog cookieja nema CSRF napada, pa je CSRF isključen
    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET, "/api/proizvodi/**", "/api/kategorije/**").permitAll()
                        .requestMatchers(HttpMethod.PATCH, "/api/narudzbe/*/status").hasRole(ADMIN)
                        .requestMatchers("/api/narudzbe/**").authenticated()
                        .anyRequest().hasRole(ADMIN))
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    // Thymeleaf stranice: prijava formom, sesija i CSRF token (th:action ga sam dodaje u svaku formu).
    // Pravila se provjeravaju redom - odlučuje prvo koje odgovara zahtjevu
    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/proizvodi/novi", "/proizvodi/*/uredi", "/kategorije/*/uredi").hasRole(ADMIN)
                        .requestMatchers(HttpMethod.GET, "/proizvodi", "/proizvodi/*", "/kategorije").permitAll()
                        .requestMatchers("/proizvodi/**", "/kategorije/**", "/narudzbe/*/status").hasRole(ADMIN)
                        .requestMatchers("/", "/error", "/prijava", "/registracija").permitAll()
                        .requestMatchers("/korpa", "/korpa/dodaj", "/korpa/*/kolicina", "/korpa/*/ukloni").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/prijava")
                        .usernameParameter("email")
                        .passwordParameter("lozinka")
                        .failureUrl("/prijava?greska")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/odjava")
                        .logoutSuccessUrl("/"));
        return http.build();
    }
}
