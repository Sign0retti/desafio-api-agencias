package br.com.desafio_santander.desafio_santander_api_v2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DesafioSantanderApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(DesafioSantanderApiApplication.class, args);
    }
}