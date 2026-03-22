package com.tvlicensing.tvlicensing.config;

import com.tvlicensing.tvlicensing.service.CustomerUserDetailsService;

// @Bean marks a method as producing a Spring-managed object
import org.springframework.context.annotation.Bean;

// @Configuration marks this class as containing Spring settings
import org.springframework.context.annotation.Configuration;

// AuthenticationManager handles the actual login verification process
import org.springframework.security.authentication.AuthenticationManager;

// AuthenticationConfiguration lets Spring wire everything together
// automatically behind the scenes
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

// HttpSecurity is the object we use to define our security rules
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// @EnableWebSecurity activates Spring Security for the whole app
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

// Used to configure the H2 console frame options
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;

// BCryptPasswordEncoder is the industry standard password encryption tool
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// PasswordEncoder is the interface BCryptPasswordEncoder implements
import org.springframework.security.crypto.password.PasswordEncoder;

// SecurityFilterChain holds all our page access and login rules
import org.springframework.security.web.SecurityFilterChain;

// @Configuration tells Spring this class contains app-wide settings
// @EnableWebSecurity activates Spring Security across the whole app
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //custom service that looks up customers from the database
    private final CustomerUserDetailsService customerUserDetailsService;

    public SecurityConfig(CustomerUserDetailsService customerUserDetailsService) {
        this.customerUserDetailsService = customerUserDetailsService;
    }

    // PASSWORD ENCODER
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AUTHENTICATION MANAGER
    // Spring automatically detects our CustomerUserDetailsService bean
    // and PasswordEncoder bean and wires them together behind the scenes
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    // SECURITY FILTER CHAIN
    // This method defines all the rules for who can access what, how login works, and how logout works
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                // AUTHORISATION RULES - who can see what
                .authorizeHttpRequests(auth -> auth

                        // These pages are PUBLIC - no login required
                        .requestMatchers("/", "/login", "/register").permitAll()

                        // Static resources are always permitted
                        .requestMatchers("/style.css", "/**.css", "/**.js", "/images/**").permitAll()

                        .requestMatchers("/h2-console/**").permitAll()

                        // Everything else requires the user to be logged in
                        .anyRequest().authenticated()
                )

                // LOGIN SETTINGS
                .formLogin(form -> form

                        // Our custom login page rather than Spring's default one
                        .loginPage("/login")

                        // Spring Security processes the submitted form at this URL
                        .loginProcessingUrl("/login")

                        // Tell Spring Security the email field is called "username" in the form
                        .usernameParameter("username")

                        // Tell Spring Security the password field is called "password"
                        .passwordParameter("password")

                        // After a successful login send the user to the dashboard
                        .defaultSuccessUrl("/dashboard", true)

                        // If login fails return to the login page with an error flag
                        .failureUrl("/login?error=true")

                        // The login page itself must always be accessible
                        .permitAll()
                )

                // LOGOUT SETTINGS
                .logout(logout -> logout

                        // Visiting /logout triggers the logout process
                        .logoutUrl("/logout")

                        // After logging out send the user to the home page
                        .logoutSuccessUrl("/")

                        // The logout URL must always be accessible
                        .permitAll()
                )

                // H2 CONSOLE FRAME FIX
                // The H2 console uses HTML frames which Spring Security
                // blocks by default - this disables that block for
                // development purposes only
                .headers(headers -> headers
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable)
                )

                // CSRF FIX FOR H2 CONSOLE ONLY
                // Disables CSRF token checking for the H2 console path only
                // CSRF protection remains active for everything else
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**")
                );

        return http.build();
    }
}
