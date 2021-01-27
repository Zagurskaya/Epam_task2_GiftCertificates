package com.epam.esm.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = "com.epam.esm.*")
@PropertySource(value = "classpath:/jdbc.properties")
public class RepositoryTestConfig {

    @Autowired
    Environment env;

    @Bean
    public DataSource dataSource() {

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(env.getProperty("test.database.driver"));
        hikariConfig.setJdbcUrl(env.getProperty("test.database.url"));
        hikariConfig.setUsername(env.getProperty("test.database.username"));
        hikariConfig.setPassword(env.getProperty("test.database.password"));

        hikariConfig.setMaximumPoolSize(10);
//        hikariConfig.setConnectionTestQuery("SELECT 1");
//        hikariConfig.setPoolName("springHikariCP");
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }

    @Bean
    public DataSourceTransactionManager dataSourceTransactionManager() {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager(dataSource());
        return dataSourceTransactionManager;
    }
}
