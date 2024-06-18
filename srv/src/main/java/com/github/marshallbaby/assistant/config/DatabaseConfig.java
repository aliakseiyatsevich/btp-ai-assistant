package com.github.marshallbaby.assistant.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource(@Value("${hana.url}") final String url,
                                 @Value("${hana.user}") final String user,
                                 @Value("${hana.password}") final String password) {


        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .driverClassName(com.sap.db.jdbc.Driver.class.getName())
                .url(url)
                .username(user)
                .password(password)
                .build();

    }

}
