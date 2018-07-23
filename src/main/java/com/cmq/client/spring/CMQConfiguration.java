package com.cmq.client.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CMQConfiguration {

	@Bean
	public CMQContainerInitializer cmqContainerInitializer() {
		return new CMQContainerInitializer();
	}
}
