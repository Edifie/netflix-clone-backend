package com.dt.netflixclonebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class NetflixCloneBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetflixCloneBackendApplication.class, args);
	}

}
