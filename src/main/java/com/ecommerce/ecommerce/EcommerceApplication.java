package com.ecommerce.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication//(exclude = DataSourceAutoConfiguration.class)
public class EcommerceApplication {

	public static void main(String[] args)  throws InterruptedException{
		// Añadir un retraso de 10 segundos
		System.out.println("Esperando a que la base de datos esté lista...");
		Thread.sleep(10000);
		SpringApplication.run(EcommerceApplication.class, args);
	}

}
