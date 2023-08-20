/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Util {

    public static void ConsoleLog(String key, String value) {
        System.out.println("*****");
        System.out.println(String.format("%s: %s", key, value));
        System.out.println("*****");
    }

    public static String encrypteMe(String dataToEncrypt) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(dataToEncrypt);
    }

    public static <T, E> E convertToDto(T originalValue, Class<E> toClass) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(originalValue, toClass);
    }

    public static String generarClave(int longitud) {
        final String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789?.,:;!@%&";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(longitud);
        for (int i = 0; i < longitud; i++) {
            int indice = random.nextInt(caracteres.length());
            sb.append(caracteres.charAt(indice));
        }
        return sb.toString();
    }
}
