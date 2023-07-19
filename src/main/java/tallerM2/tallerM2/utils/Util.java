/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Util {
    public static void ConsoleLog(String key, String value){
        System.out.println("*****");
        System.out.println(String.format("%s: %s",key,value));
        System.out.println("*****");
    }

    // public static String encrypteMe(String dataToEncrypt) {
    //     BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    //     return encoder.encode(dataToEncrypt);
    // }

    public static <T,E> E convertToDto(T originalValue, Class<E> toClass){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.convertValue(originalValue, toClass);
    }
}
