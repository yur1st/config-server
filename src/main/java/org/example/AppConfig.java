package org.example;

import com.impossibl.postgres.jdbc.PGConnectionPoolDataSource;
import com.impossibl.postgres.jdbc.PGDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import java.time.Duration;

@Configuration
public class AppConfig {

    @Value("${app.datasource.url}")
    private String dataSourceUrl;
    @Value("${app.datasource.username}")
    private String user;
    @Value("${app.datasource.password}")
    private String pass;

    @Bean
    public DataSource dataSource() {
        PGDataSource dataSource= new PGDataSource();
        dataSource.setDatabaseUrl(dataSourceUrl);
        dataSource.setUser(user);
        dataSource.setPassword(pass);
        return dataSource;
    }

    private static final int TIMEOUT = 500;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(TIMEOUT))
                .setReadTimeout(Duration.ofMillis(TIMEOUT)).build();
    }

}
