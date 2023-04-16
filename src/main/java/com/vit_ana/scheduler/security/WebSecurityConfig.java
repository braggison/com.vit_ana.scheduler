package com.vit_ana.scheduler.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

//@EnableWebSecurity
//@Configuration
//@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfiguration {

    private final CustomUserDetailsService customUserDetailsService;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final PasswordEncoder passwordEncoder;

    public WebSecurityConfig(CustomUserDetailsService customUserDetailsService, CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler, PasswordEncoder passwordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.passwordEncoder = passwordEncoder;
    }

//    protected void configure(AuthenticationManagerBuilder auth) {
//        auth.authenticationProvider(authenticationProvider());
//    }
//
//    @Bean
////    protected void configure(HttpSecurity http) throws Exception {
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf().disable()
//                .authorizeHttpRequests()
//                .requestMatchers("/").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
//                .requestMatchers("/api/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
//                .requestMatchers("/customers/all").hasRole("ADMIN")
//                .requestMatchers("/providers/new").hasRole("ADMIN")
//                .requestMatchers("/invoices/all").hasRole("ADMIN")
//                .requestMatchers("/providers/all").hasRole("ADMIN")
//                .requestMatchers("/customers/**").hasAnyRole("CUSTOMER", "ADMIN")
//                .requestMatchers("/providers/availability/**").hasRole("PROVIDER")
//                .requestMatchers("/providers/**").hasAnyRole("PROVIDER", "ADMIN")
//                .requestMatchers("/works/**").hasRole("ADMIN")
//                .requestMatchers("/exchange/**").hasAnyRole("CUSTOMER")
//                .requestMatchers("/appointments/new/**").hasRole("CUSTOMER")
//                .requestMatchers("/appointments/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
//                .requestMatchers("/invoices/**").hasAnyRole("CUSTOMER", "PROVIDER", "ADMIN")
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/perform_login")
//                .successHandler(customAuthenticationSuccessHandler)
//                .permitAll()
//                .and()
//                .logout().logoutUrl("/perform_logout")
//                .and()
//                .exceptionHandling().accessDeniedPage("/access-denied");
//        
//        return http.build();
//    }

//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().requestMatchers("/customers/new/**");
//    }

//    @Bean
//    public DaoAuthenticationProvider authenticationProvider() {
//        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
//        auth.setUserDetailsService(customUserDetailsService);
//        auth.setPasswordEncoder(passwordEncoder);
//        return auth;
//    }
}