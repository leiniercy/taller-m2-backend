/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import tallerM2.tallerM2.exceptions.ErrorObject;
import tallerM2.tallerM2.exceptions.custom.BadRequest;
import tallerM2.tallerM2.exceptions.custom.Conflict;
import tallerM2.tallerM2.exceptions.custom.ValueNotFound;



// Custom Errors. Aquí manejamos todos los erroes que pueden surgir en la aplicación y le respuesta que deben enviar.
@RestControllerAdvice
public class ErrorHandler {
    
   @ResponseStatus(HttpStatus.NOT_FOUND)
   @ExceptionHandler(ValueNotFound.class)
   public ResponseEntity<?> notfound(Throwable throwable) {
       return new ResponseEntity<>(
               new ErrorObject(HttpStatus.NOT_FOUND.toString(), /*errorDescription*/throwable.getMessage()), HttpStatus.NOT_FOUND);
   }
   @ResponseStatus(HttpStatus.BAD_REQUEST)
   @ExceptionHandler(BadRequest.class)
   public ResponseEntity<?> badRequest(Throwable throwable) {
       return new ResponseEntity<>(
               new ErrorObject(HttpStatus.BAD_REQUEST.toString(), /*errorDescription*/throwable.getMessage()),
               HttpStatus.BAD_REQUEST);
   }
   @ResponseStatus(HttpStatus.CONFLICT)
   @ExceptionHandler(Conflict.class)
   private ResponseEntity<?> conflict(Throwable throwable) {
       return new ResponseEntity<>(
               new ErrorObject(HttpStatus.CONFLICT.toString(), /*errorDescription*/throwable.getMessage()),
               HttpStatus.CONFLICT
       );
   }

}
