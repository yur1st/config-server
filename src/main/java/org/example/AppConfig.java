package org.example;

import com.impossibl.postgres.jdbc.PGDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.config.server.environment.JdbcEnvironmentProperties;
import org.springframework.cloud.config.server.environment.JdbcEnvironmentRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.time.Duration;

@Configuration
public class AppConfig {

    private static final int TIMEOUT = 500;

    @Value("${app.datasource.url}")
    private String dataSourceUrl;
    @Value("${app.datasource.username}")
    private String user;
    @Value("${app.datasource.password}")
    private String pass;

    @Bean
    JdbcEnvironmentRepository jdbcEnvironmentRepository(JdbcTemplate template, JdbcEnvironmentProperties properties) {
        return new JdbcEnvironmentRepository(template, properties);
    }

    @Bean
    public DataSource dataSource() {
        PGDataSource dataSource = new PGDataSource();
        dataSource.setDatabaseUrl(dataSourceUrl);
        dataSource.setUser(user);
        dataSource.setPassword(pass);
        return dataSource;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(TIMEOUT))
                .setReadTimeout(Duration.ofMillis(TIMEOUT)).build();
    }
}
