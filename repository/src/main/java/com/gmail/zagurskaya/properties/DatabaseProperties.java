package com.gmail.zagurskaya.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DatabaseProperties {

    @Value("${database.url}")
    private String url;
    @Value("${database.username:}")
    private String username;
    @Value("${database.password:}")
    private String password;
    @Value("${database.driver}")
    private String driver;

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDriver() {
        return driver;
    }
}
