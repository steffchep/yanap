package com.topdesk.yanap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;

import com.topdesk.yanap.database.Sprint;
import com.topdesk.yanap.database.User;

@SpringBootApplication
public class Main extends RepositoryRestMvcConfiguration {
	
	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(User.class);
		config.exposeIdsFor(Sprint.class);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}
	
}
