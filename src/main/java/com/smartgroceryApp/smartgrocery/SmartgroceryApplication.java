package com.smartgroceryApp.smartgrocery;

import com.smartgroceryApp.smartgrocery.security.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({
		JwtProperties.class,
})
public class SmartgroceryApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartgroceryApplication.class, args);
	}

}
