
package com.valoores.cassandradatacrowd;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient 
@SpringBootApplication
public class CassandraDatacrowdApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(CassandraDatacrowdApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }

}