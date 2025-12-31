package IngressApplication.IngressApplication.utils.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler{
    @ExceptionHandler(QueueFullException.class)
    public ResponseEntity<Object> handleQueueFullException(QueueFullException ex) {
            return ResponseEntity
                    .status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(new CustomResponse(ex.getMessage(), HttpStatus.TOO_MANY_REQUESTS.value(), new int[0]));
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    public ResponseEntity<Object> handleServiceUnavailableException(ServiceUnavailableException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new CustomResponse(ex.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value(), new int[0]));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CustomResponse> handleValidation(MethodArgumentNotValidException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CustomResponse(
                        "Invalid request",
                        HttpStatus.BAD_REQUEST.value(),
                        null
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CustomResponse> handleBadJson(HttpMessageNotReadableException ex) {

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CustomResponse(
                        "Malformed JSON",
                        HttpStatus.BAD_REQUEST.value(),
                        null
                ));
    }

    @ExceptionHandler(EmptyQueueException.class)
    public ResponseEntity<?> handleEmptyQueueException(EmptyQueueException ex) {
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT).build();
    }
}
