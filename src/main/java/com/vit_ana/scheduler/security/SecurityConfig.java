package com.vit_ana.scheduler.security;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	@Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf().disable()
		.authorizeHttpRequests((authorize) -> authorize
	            .requestMatchers("/css/**", "/js/**", "/img/**", "/webjars/**").permitAll()
	            .requestMatchers("/customers/new/**").permitAll()
	            .requestMatchers("/customers/all").hasRole("ADMIN")
	            .requestMatchers("/").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
	            .requestMatchers("/api/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
	            .requestMatchers("/providers/new").hasRole("ADMIN")
	            .requestMatchers("/invoices/all").hasRole("ADMIN")
	            .requestMatchers("/providers/all").hasRole("ADMIN")
	            .requestMatchers("/customers/**").hasAnyRole("CUSTOMER", "ADMIN")
	            .requestMatchers("/providers/availability/**").hasRole("PROVIDER")
	            .requestMatchers("/providers/**").hasAnyRole("PROVIDER", "ADMIN")
	            .requestMatchers("/works/**").hasRole("ADMIN")
	            .requestMatchers("/exchange/**").hasAnyRole("CUSTOMER")
	            .requestMatchers("/appointments/new/**").hasAnyRole("CUSTOMER")
	            .requestMatchers("/appointments/add-slot/**").hasAnyRole("PROVIDER")
	            .requestMatchers("/appointments/get-slot/**").hasAnyRole("CUSTOMER")
	            .requestMatchers("/appointments/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
	            .requestMatchers("/invoices/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
				.anyRequest().authenticated()
		)
	            .formLogin()
	            .loginPage("/login")
	            .loginProcessingUrl("/perform_login")
	            .successHandler(customAuthenticationSuccessHandler)
	            .permitAll()
	            .and()
	            .logout().logoutUrl("/perform_logout")
	            .and()
	            .exceptionHandling().accessDeniedPage("/access-denied");
	    return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(
			HttpSecurity http,
			PasswordEncoder passwordEncoder,
			CustomUserDetailsService customUserDetailsService)
      throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
          .userDetailsService(customUserDetailsService)
          .passwordEncoder(passwordEncoder)
          .and()
          .build();
    }
}
