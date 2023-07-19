/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tallerM2.tallerM2.exceptions.custom;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Admin
 */
// Esto es para crear exceptions personalizadas
@ResponseStatus(value = HttpStatus.CONFLICT)
public class Conflict extends Exception{
    public Conflict(){}
    public Conflict(String msg){super(msg);}
}