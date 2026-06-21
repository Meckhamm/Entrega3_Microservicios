package com.inventario.clduoc.Inventario.exception;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public @ResponseBody Map<String, Object> manejarNotFound(ResourceNotFoundException ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("codigo", 404);
        error.put("mensaje", ex.getMessage());
        error.put("fecha", LocalDateTime.now());

        return error;
    }

    @ExceptionHandler(FeignException.NotFound.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public @ResponseBody Map<String, Object> manejarProductoNoExiste(FeignException.NotFound ex) {

        Map<String, Object> error = new HashMap<>();

        error.put("codigo", 400);
        error.put("mensaje", "El producto no existe, primero debes crear uno");
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
        error.put("mensaje", "Debes especificar una ID a actualizar");
        error.put("fecha", LocalDateTime.now());

        return error;
    }
}