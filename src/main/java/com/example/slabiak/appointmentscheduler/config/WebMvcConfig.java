package com.example.slabiak.appointmentscheduler.config;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private VersionInterceptor versionInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(versionInterceptor);
        registry.addInterceptor(localeInterceptor());
    }
    
	@Bean
	public LocaleChangeInterceptor localeInterceptor() {
		final LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
		localeInterceptor.setParamName("lang");
		return localeInterceptor;
	}

	@Bean
	public LocaleResolver localeResolver() {
		final SessionLocaleResolver clr = new SessionLocaleResolver();
		clr.setDefaultLocale(new Locale("ru"));
		return clr;
	}
}