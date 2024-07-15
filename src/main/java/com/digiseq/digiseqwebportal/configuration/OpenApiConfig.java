package com.digiseq.digiseqwebportal.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(
            new Info()
                .title("Digiseq Web Portal")
                .version("1.0")
                .description(
                    "A Web Portal Application for managing and updating client organisations and their personnel"));
  }
}
