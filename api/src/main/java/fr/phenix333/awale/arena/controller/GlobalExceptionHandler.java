package fr.phenix333.awale.arena.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import fr.phenix333.logger.MyLogger;

/*
 * Global exception handler for the Awale Arena application.
 * 
 * @author Colin de Seroux
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final MyLogger L = MyLogger.create(GlobalExceptionHandler.class);

    /**
     * Handler for IllegalArgumentException.
     * 
     * @param e -> IllegalArgumentException : the exception to handle
     * 
     * @return ResponseEntity<String> -> With BAD_REQUEST status and the exception
     *         message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    /**
     * General handler for exceptions.
     * 
     * @param e -> Exception : the exception to handle
     * 
     * @return ResponseEntity<String> -> With INTERNAL_SERVER_ERROR status and a
     *         generic error message
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<String> handleGenericException(Exception e) {
        L.error("An exception is raised", e);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("OopsError");
    }

}
