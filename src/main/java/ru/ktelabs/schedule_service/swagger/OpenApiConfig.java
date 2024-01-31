package ru.ktelabs.schedule_service.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(
                        new io.swagger.v3.oas.models.info.Info()
                                .title("Clinic Schedule Api")
                                .description("Schedule Api for KTE Labs")
                                .version("1.0.0")
                                .contact(
                                        new io.swagger.v3.oas.models.info.Contact()
                                                .email("timur@mailbox66.ru")
                                                .url("https://github.com/TimurDavletgareev")
                                                .name("Davletgareev Timur")
                                )
                );
    }

}
