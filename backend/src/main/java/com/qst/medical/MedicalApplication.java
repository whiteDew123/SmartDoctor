package com.qst.medical;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.qst.medical")
@EnableTransactionManagement
public class MedicalApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalApplication.class, args);
    }
}