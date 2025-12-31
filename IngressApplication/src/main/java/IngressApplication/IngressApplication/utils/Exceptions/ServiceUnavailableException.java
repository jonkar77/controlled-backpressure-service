package IngressApplication.IngressApplication.utils.Exceptions;

public class ServiceUnavailableException extends RuntimeException{
    public ServiceUnavailableException(String message){
        super(message);
    }
}
