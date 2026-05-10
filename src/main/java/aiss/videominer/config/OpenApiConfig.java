package aiss.videominer.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "VideoMiner API",
                version = "v1",
                description = "REST API for retrieving, consulting and storing channels data",
                contact = @Contact(
                        url = "https://github.com/AlbertoCV03/VideoMiner"
                )
        )
)
public class OpenApiConfig {
}
