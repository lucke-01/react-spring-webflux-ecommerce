package com.ricardocreates.infra.server.rest.controller.v1;

import com.ricardocreates.domain.entity.exceptions.BadRequestException;
import com.ricardocreates.domain.entity.exceptions.ConflictException;
import com.ricardocreates.domain.entity.exceptions.NotFoundException;
import com.ricardocreates.domain.entity.exceptions.ServiceUnavailableException;
import com.ricardocreates.domain.entity.exceptions.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferLimitException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

@RequiredArgsConstructor
@ControllerAdvice
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RestExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<String> handleEntityNotFound(final NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getDescription());
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<String> handleEntityBadRequest(final BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getDescription());
    }

    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<String> handleEntityConflict(final ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getDescription());
    }

    @ExceptionHandler(ServiceUnavailableException.class)
    protected ResponseEntity<String> handleEntityServiceUnavailable(final ServiceUnavailableException ex) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getDescription());
    }

    @ExceptionHandler(UnauthorizedException.class)
    protected ResponseEntity<String> handleEntityUnauthorized(final UnauthorizedException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getDescription());
    }

    @ExceptionHandler(WebExchangeBindException.class)
    protected ResponseEntity<String> handleEntityServiceUnavailable(final WebExchangeBindException webBindException) {
        StringBuilder errorsStringBuilder = new StringBuilder(100);
        for (int index = 0; index < webBindException.getBindingResult().getFieldErrors().size(); index++) {
            FieldError fieldError = webBindException.getBindingResult().getFieldErrors().get(index);
            errorsStringBuilder.append(String.format("%s(%s): %s", fieldError.getField(), fieldError.getCode(), fieldError.getDefaultMessage()));
            if ((index + 1) < webBindException.getBindingResult().getFieldErrors().size()) {
                errorsStringBuilder.append("\n");
            }
        }
        String errorsString = errorsStringBuilder.isEmpty() ? "Bad request" : errorsStringBuilder.toString();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsString);
    }

    /**
     * Used when requests are too big.
     * But in our app case when a request which has a field which is a file in base64 is too long
     * in this case we show an error saying that the request is too long. thus it is possible a file or files size problem
     *
     * @param bufferLimitException
     * @return
     */
    @ExceptionHandler
    protected ResponseEntity<String> handleBufferExceededSize(final DataBufferLimitException bufferLimitException) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Files exceeds the limit size per request. Please reduce size of files.");
    }

}
