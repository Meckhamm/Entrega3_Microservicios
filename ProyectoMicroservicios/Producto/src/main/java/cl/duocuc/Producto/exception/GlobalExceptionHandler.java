package cl.duocuc.Producto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public @ResponseBody Map<String, Object> manejarResponseStatus(ResponseStatusException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("codigo", ex.getStatusCode().value());
        error.put("mensaje", ex.getReason());
        error.put("fecha", LocalDateTime.now());

        return error;
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody Map<String, Object> manejarRutaIncorrecta(NoHandlerFoundException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("codigo", 404);
        error.put("mensaje", "La ruta ingresada no existe");
        error.put("fecha", LocalDateTime.now());

        return error;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody Map<String, Object> manejarGeneral(Exception ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("codigo", 400);
        error.put("mensaje", "Ocurrió un error en la solicitud");
        error.put("detalle", ex.getMessage());
        error.put("fecha", LocalDateTime.now());

        return error;
    }
}