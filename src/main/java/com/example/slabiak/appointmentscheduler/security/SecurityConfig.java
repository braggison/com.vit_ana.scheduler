package com.example.slabiak.appointmentscheduler.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfig {

	@Autowired
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
	@Autowired
    private CustomUserDetailsService customUserDetailsService;
	@Autowired
    private PasswordEncoder passwordEncoder;

	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
	    http.csrf().disable()
	            .authorizeHttpRequests()
	            .requestMatchers("/").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
	            .requestMatchers("/api/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
	            .requestMatchers("/customers/all").hasRole("ADMIN")
	            .requestMatchers("/providers/new").hasRole("ADMIN")
	            .requestMatchers("/invoices/all").hasRole("ADMIN")
	            .requestMatchers("/providers/all").hasRole("ADMIN")
	            .requestMatchers("/customers/**").hasAnyRole("CUSTOMER", "ADMIN")
	            .requestMatchers("/providers/availability/**").hasRole("PROVIDER")
	            .requestMatchers("/providers/**").hasAnyRole("PROVIDER", "ADMIN")
	            .requestMatchers("/works/**").hasRole("ADMIN")
	            .requestMatchers("/exchange/**").hasAnyRole("CUSTOMER")
	            .requestMatchers("/appointments/new/**").hasRole("CUSTOMER")
	            .requestMatchers("/appointments/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
	            .requestMatchers("/invoices/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
	            .and()
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
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return (web) -> web
	      .ignoring()
          .requestMatchers("/css/**")
          .requestMatchers("/js/**")
          .requestMatchers("/img/**")
	      .requestMatchers("/customers/new/**");
	}
	
//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
//        auth.setUserDetailsService(customUserDetailsService);
//        auth.setPasswordEncoder(passwordEncoder);
//        return auth;
//    }
    
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailService) 
      throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
          .userDetailsService(customUserDetailsService)
          .passwordEncoder(passwordEncoder)
          .and()
          .build();
    }
}
