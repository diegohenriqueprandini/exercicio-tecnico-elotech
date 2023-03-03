package com.diego.prandini.exerciciotecnicoelotech.infra.controller;

import com.diego.prandini.exerciciotecnicoelotech.infra.system.clock.ApplicationClock;
import com.diego.prandini.exerciciotecnicoelotech.utils.JsonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final ApplicationClock applicationClock;

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final var details = createDefaultExceptionDetails(e, request);
        return ResponseEntity
                .status(status)
                .headers(headers)
                .body(details);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<ControllerErrorData> runtimeException(RuntimeException e, WebRequest request) {
        final var details = createDefaultExceptionDetails(e, request);
        HttpStatus httpStatus = ExceptionHandlerMap.NOT_FOUND.contains(e.getClass()) ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
        return createDefaultResponseByException(details, httpStatus);
    }

    @ExceptionHandler(JsonUtils.JsonUtilsException.class)
    public final ResponseEntity<ControllerErrorData> jsonUtilsException(JsonUtils.JsonUtilsException e, WebRequest request) {
        final var details = createDefaultExceptionDetails(e, request);
        return createDefaultResponseByException(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ControllerErrorData createDefaultExceptionDetails(Exception e, WebRequest request) {
        return ControllerErrorData.builder()
                .timestamp(applicationClock.now())
                .message(e.getMessage())
                .detail(createDetail(request))
                .build();
    }

    private String createDetail(WebRequest request) {
        String httpMethod = "method=" + ((ServletWebRequest) request).getHttpMethod();
        String uri = request.getDescription(false);
        return httpMethod + "," + uri;
    }

    private ResponseEntity<ControllerErrorData> createDefaultResponseByException(ControllerErrorData details, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(details);
    }
}
