package com.topdesk.yanap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@EntityScan(
		basePackageClasses = { Main.class, Jsr310JpaConverters.class }
)
@SpringBootApplication
public class Main extends WebMvcConfigurerAdapter {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/more/**").addResourceLocations("file:./additionalFiles/");
	}

	public static void main(String[] args) {
		SpringApplication.run(Main.class);
	}
	
}
