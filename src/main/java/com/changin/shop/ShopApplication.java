package com.changin.shop;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
public class ShopApplication {

    static{
        Dotenv dotenv = Dotenv.load();
        //Property에 읽어온 env 환경 변수 값 등록.
        dotenv.entries().forEach(e ->
                System.setProperty(e.getKey(), e.getValue())
        );

        // 값 읽기
        String username = dotenv.get("DB_USERNAME");
        String password = dotenv.get("DB_PASSWORD");

        System.out.println("============> DB_USERNAME: " + username);
        System.out.println("============> DB_PASSWORD: " + password);
    }

	public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
	}

}
