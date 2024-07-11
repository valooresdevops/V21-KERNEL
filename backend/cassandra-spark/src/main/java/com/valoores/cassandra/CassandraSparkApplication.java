package com.valoores.cassandra;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
//@EntityScan("com.valoores.cassandra.app.model")
//@ComponentScan("com.valoores.cassandra.app.model")
public class CassandraSparkApplication {
  
	public static void main(String[] args) {
		SpringApplication.run(CassandraSparkApplication.class, args);

	}

}
