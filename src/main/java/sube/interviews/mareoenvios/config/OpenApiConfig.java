package sube.interviews.mareoenvios.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public OpenAPI customOpenAPI() {
      return new OpenAPI()
              .info(new Info()
                      .title("Mareo Envíos API")
                      .version("v1")
                      .description("API para la gestión de envíos de mercadería"))
              .servers(List.of(new Server().url("/")));
  }
}