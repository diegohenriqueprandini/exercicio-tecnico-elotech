package com.diego.prandini.exerciciotecnicoelotech.infra.config;

import com.diego.prandini.exerciciotecnicoelotech.infra.properties.ApplicationProperties;
import com.diego.prandini.exerciciotecnicoelotech.utils.JsonUtils;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableConfigurationProperties({ApplicationProperties.class})
public class ApiConfig implements WebMvcConfigurer {

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
        return JsonUtils.defaultJackson2ObjectMapperBuilder();
    }

    @Bean
    public OpenAPI customOpenApi(ApplicationProperties applicationProperties) {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationProperties.getTitle())
                        .version(applicationProperties.getVersion())
                        .description("exercicio-tecnico-elotech"));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry
                .addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .maxAge(3600);
    }
}
