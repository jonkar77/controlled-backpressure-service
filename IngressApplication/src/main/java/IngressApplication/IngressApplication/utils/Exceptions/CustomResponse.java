package IngressApplication.IngressApplication.utils.Exceptions;

import org.springframework.http.ResponseEntity;

import java.util.ArrayList;

public class CustomResponse {
    public String message;
    public int status;
    public Object data;

    public CustomResponse(String message, int status, Object data) {
        this.message = message;
        this.status = status;
        this.data = data;
    }
}
