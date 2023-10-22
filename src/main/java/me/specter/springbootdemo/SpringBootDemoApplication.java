package me.specter.springbootdemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SpringBootDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDemoApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer(@Value(value = "${application.allowed-origin}") String allowedOrigin) {
		return new WebMvcConfigurer(){
			public void addCorsMappings(CorsRegistry registry){
				registry
				.addMapping("/**")
				.allowedMethods("*")
				.allowedOrigins(allowedOrigin)
				;
			}
		};
	}

}
