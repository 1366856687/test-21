package com.test.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * SpringDoc OpenAPI 3 配置类
 * 对应 Swagger 界面：http://localhost:8080/swagger-ui.html
 */
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("项目 API 文档")
                        .version("1.0.0")
                        .description("Spring Boot 3.5.11 项目接口文档")
                        .contact(new Contact()
                                .name("你的名字")
                                .email("your@email.com")
                        )
                );
    }
}
