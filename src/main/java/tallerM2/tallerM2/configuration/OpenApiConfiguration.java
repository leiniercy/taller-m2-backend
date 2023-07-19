/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI customCofiguration() {
        Contact c = new Contact();
        c.setName("Leinier Caraballo Yanes");
        c.setEmail("leiniercaraballo08@gmail.com");
        c.setUrl("https://leiniercy.github.com/");
        return new OpenAPI()
                .components(new Components())
                .info(new Info().title("API Docs")
                        .description("REST API documentation")
                        .contact(c)
                );
    }
}
