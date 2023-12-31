package tallerM2.tallerM2.configuration;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {

    @Value("${minio.url}")
    private String url;

    @Value("${minio.accessKey}")
    private String accessKey;

    @Value("${minio.secretKey}")
    private String secretKey;

    @Value("${minio.bucketName}")
    private String bucketName;

    @Bean
    public MinioClient generateMinioClient() {
        try {
            MinioClient client = new MinioClient(url, accessKey, secretKey);
            return client;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}

