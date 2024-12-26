package sube.interviews.mareoenvios.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseInitializer.class);
    private final JdbcTemplate jdbcTemplate;
    private final ResourceLoader resourceLoader;
    private boolean databaseInitialized = false;

    @Autowired
    public DatabaseInitializer(JdbcTemplate jdbcTemplate, ResourceLoader resourceLoader) {
        this.jdbcTemplate = jdbcTemplate;
        this.resourceLoader = resourceLoader;
    }


    @Override
    public void run(String... args) throws Exception {
        if (!databaseInitialized) {
            log.info("Database initialization started");
            try {
                executeSqlScript("classpath:sql/00_schema.sql");
                executeSqlScript("classpath:sql/01_data.sql");
                databaseInitialized = true;
                log.info("Database initialization completed");
            } catch(Exception e) {
                log.error("Error during database initialization", e);
            }
        }
    }

    private void executeSqlScript(String scriptPath) {
        log.info("Executing SQL script: {}", scriptPath);
        Resource resource = resourceLoader.getResource(scriptPath);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            String sql = reader.lines().collect(Collectors.joining("\n"));
            jdbcTemplate.execute(sql);
            log.info("SQL script executed successfully: {}", scriptPath);
        } catch (IOException e) {
            log.error("Error executing sql script: " + scriptPath, e);
            throw new RuntimeException("Error executing sql script: " + scriptPath , e);
        }
    }
}