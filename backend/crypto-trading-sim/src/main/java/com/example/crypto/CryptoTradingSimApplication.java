package com.example.crypto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication
@ComponentScan(
		basePackages = "com.example.crypto",
		excludeFilters = @ComponentScan.Filter(
				type = FilterType.REGEX,
				pattern = "com\\.example\\.crypto\\.websocket\\.server\\..*"
		)
)
public class CryptoTradingSimApplication {

	public static void main(String[] args) {
		SpringApplication.run(CryptoTradingSimApplication.class, args);
	}

}
