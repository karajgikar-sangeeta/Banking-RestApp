package com.banking.svkbanking;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingApplication {
	
	private static final Logger logger = LoggerFactory.getLogger(BankingApplication.class);

	public static void main(String[] args) {
		logger.info("Starting the SVK Banking API process on port 8080");
		SpringApplication.run(BankingApplication.class, args);
	}
}
