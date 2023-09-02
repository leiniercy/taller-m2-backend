package tallerM2.tallerM2.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@Configuration
public class CorsConfiguration {

    @Value("${taller2M.app.allowed.origin.local}")
    private String url_local;
    @Value("${taller2M.app.allowed.origin.ext}")
    private String url_ext;
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedMethods("GET","PUT","POST","DELETE") //tipos de peticiones aceptadas
                        .allowedOrigins(url_local,url_ext); // url de donde se reciben las peticiones
            }

        };

    }

}
