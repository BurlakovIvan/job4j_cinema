package ru.job4j.cinema.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * Класс конфигурирования
 * @author Burlakov
 */
@Configuration
@PropertySource("classpath:db.properties")
public class DataSourceConfig {

    /**
     * загрузка пула
     * @param driver jdbc.driver
     * @param url jdbc.url
     * @param username jdbc.username
     * @param password jdbc.password
     * @return пул подключений
     */
    @Bean
    public DataSource loadPool(@Value("${jdbc.driver}") String driver,
                               @Value("${jdbc.url}") String url,
                               @Value("${jdbc.username}") String username,
                               @Value("${jdbc.password}") String password) {
        BasicDataSource pool = new BasicDataSource();
        pool.setDriverClassName(driver);
        pool.setUrl(url);
        pool.setUsername(username);
        pool.setPassword(password);
        return pool;
    }

}