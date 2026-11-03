package fr.asr.calculator.exception;

import static fr.asr.calculator.exception.ExceptionType.CONSTRAINT_VIOLATION_ERROR;

import fr.asr.calculator.data.dto.error.ErrorResponseDTO;
import fr.asr.calculator.exception.functionnal.*;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.constraints.NotNull;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleNotFoundException(@NotNull NotFoundException exception) {
        log.debug("Capture NotFoundException", exception);

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error(exception.getExceptionType())
                .details(exception.getDebugInfo())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({
        InternalException.class,
        CadenceException.class,
        CalculateException.class,
        AuthException.class,
        ProfileException.class,
        EstimateException.class,
        ExportException.class
    })
    public ResponseEntity<ErrorResponseDTO> handleOther(@NotNull FunctionalException exception) {
        log.error("Capture {}", exception.getClass().getName(), exception);

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .error(exception.getExceptionType())
                .details(exception.getDebugInfo())
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraint(@NotNull ConstraintViolationException exception) {
        log.debug("Capture {}", exception.getClass().getName(), exception);

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error(CONSTRAINT_VIOLATION_ERROR)
                .details(exception.getConstraintViolations().stream()
                        .collect(Collectors.toMap(
                                violation -> violation.getPropertyPath().toString(), ConstraintViolation::getMessage)))
                .timestamp(System.currentTimeMillis())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
