package jp.co.axa.apidemo.infrastructure.configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    type = SecuritySchemeType.HTTP,
    name = "basicAuth",
    scheme = "basic")
public class SpringdocConfig {
  @Bean
  public OpenAPI employeeAPI() {
    return new OpenAPI()
        .info(new Info().title("Employee API")
            .description("Company challenge demo API")
            .version("v0.0.1")
            .license(new License().name("License").url("http://springdoc.org")))
        .externalDocs(new ExternalDocumentation()
            .description("Wiki Documentation")
            .url("https://wiki.sample.co.jp/employee"));
  }
}
