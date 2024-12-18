package sube.interviews.mareoenvios.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {


   @Autowired
   private DataSource dataSource;


   @Value("classpath:schema.sql")
    private Resource schemaScript;

    @Value("classpath:data.sql")
    private Resource dataScript;


//  @Bean
//  @ConditionalOnProperty(name = "app.init.db", havingValue = "true", matchIfMissing = true)
//    public DataSourceInitializer dataSourceInitializer(){
//       DataSourceInitializer initializer = new DataSourceInitializer();
//       initializer.setDataSource(dataSource);
//       initializer.setDatabasePopulator(databasePopulator());
//       return initializer;
//    }
//
//    private ResourceDatabasePopulator databasePopulator() {
//         ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
//         populator.setIgnoreFailedDrops(true);
//         populator.addScript(schemaScript);
//         populator.addScript(dataScript);
//         return populator;
//    }
    
}
