/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.services.servicesImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ImageService {

    private final String FOLDER_PATH = System.getProperty("user.dir") + "\\src\\main\\resources\\static\\image\\";

    public String getURLFolderPath(){
        return FOLDER_PATH;
    }

    /*Guardar archivo en el servidor*/
    public String guardarArchivo(MultipartFile archivo) throws IOException {
        String nombreArchivo = UUID.randomUUID().toString() + "." + obtenerExtension(archivo.getOriginalFilename());
        Path rutaArchivo = Path.of(FOLDER_PATH, nombreArchivo);
        Files.copy(archivo.getInputStream(), rutaArchivo, StandardCopyOption.REPLACE_EXISTING);
        return nombreArchivo;
    }

    public String obtenerExtension(String nombreArchivo) {
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1);
    }

    /*Guardar archivo en el servidor*/
    public byte[] obtenerImagen(String nombreArchivo) throws IOException {
        Path rutaArchivo = Path.of(FOLDER_PATH, nombreArchivo);
        return Files.readAllBytes(rutaArchivo);
    }

}
