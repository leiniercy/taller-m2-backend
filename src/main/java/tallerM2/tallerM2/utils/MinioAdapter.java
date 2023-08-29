package tallerM2.tallerM2.utils;

//  package is mission
import io.minio.MinioClient;
import io.minio.messages.Bucket;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ByteArrayResource;

@Log4j2
@Service
public class MinioAdapter {

    @Autowired
    MinioClient minioClient;

    @Value("${minio.bucketName}")
    String bucketName;

    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public void uploadFile(String name, InputStream inputStream, Long size) {

        try {
            minioClient.putObject(bucketName, name, inputStream, size, null, null, null);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    public ByteArrayResource getFile(String name) {
        // Obtener el objeto de MinIO utilizando el nombre de imagen almacenado
        ByteArrayResource resource = null;
        try {
            InputStream inputStream = minioClient.getObject(bucketName, name);
            resource = new ByteArrayResource(IOUtils.toByteArray(inputStream));
        } catch (Exception ex) {
            log.error("Error: ", ex.getMessage());
        }
        return resource;
    }

    public void deleteFile(String name) {
        try {
            minioClient.removeObject(bucketName, name);
        } catch (Exception ex) {
            log.error("Error: "+ex.getMessage());
        }
    }

    @PostConstruct
    public void init() {
    }
}
