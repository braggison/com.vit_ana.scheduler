package com.vit_ana.scheduler.config;

import java.util.Properties;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BuildPropertiesConfig {

	@ConditionalOnMissingBean(BuildProperties.class)
	@Bean
	public BuildProperties buildProperties() {
		Properties properties = new Properties();
		properties.put("group", "com.vit-ana");
		properties.put("artifact", "scheduler");
		properties.put("version", "not-jarred");
		return new BuildProperties(properties);
	}
}
