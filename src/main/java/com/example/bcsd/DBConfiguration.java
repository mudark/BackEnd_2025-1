package com.example.bcsd;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Configuration
public class DBConfiguration {

    private String driveClassName;
    private String username;
    private String password;
    private String url;

    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public HikariDataSource getDataSource() {

        this.driveClassName="com.mysql.cj.jdbc.Driver";
        this.username="root";
        this.password="mudark0204;";
        this.url="jdbc:mysql://localhost:3306/bcsd?serverTimezone=Asia/Seoul";

        return DataSourceBuilder.create()
                .driverClassName(this.driveClassName)
                .url(this.url)
                .username(this.username)
                .password(this.password)

                .type(HikariDataSource.class)
                .build();
    }
}
