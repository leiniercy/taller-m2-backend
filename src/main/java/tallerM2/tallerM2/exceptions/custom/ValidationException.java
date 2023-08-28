package tallerM2.tallerM2.exceptions.custom;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class ValidationException extends Exception{
    public ValidationException(){}
    public ValidationException(String msg){super(msg);}
}
