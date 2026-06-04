package kr.ac.hansung.hellospringboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/products/*/edit").hasRole("ADMIN")
                .requestMatchers("/", "/products", "/css/**", "/js/**").permitAll()
                .requestMatchers("/api/**").permitAll() // REST API endpoints are public
                .anyRequest().authenticated()
            )
            .formLogin(form -> form.permitAll())
            .logout(logout -> logout.logoutSuccessUrl("/products").permitAll())
            .csrf(csrf -> csrf.disable()) // Disable CSRF for REST API ease of use
            .headers(headers -> headers.frameOptions(frame -> frame.disable()));
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.withUsername("admin")
            .password("{noop}admin123")
            .roles("ADMIN")
            .build();
        UserDetails user = User.withUsername("user")
            .password("{noop}user123")
            .roles("USER")
            .build();
        return new InMemoryUserDetailsManager(admin, user);
    }
}
