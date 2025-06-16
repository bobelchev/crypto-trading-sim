package com.example.crypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@EnableFeignClients
public class CryptoTradingSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoTradingSimApplication.class, args);
	}

}
