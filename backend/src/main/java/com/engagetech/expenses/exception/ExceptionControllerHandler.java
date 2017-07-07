package com.engagetech.expenses.exception;

import java.util.Map;

import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionControllerHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ErrorAttributes errorAttributes;

    @ExceptionHandler(value = {
            ValidationException.class,
            HttpMessageNotReadableException.class,
            IllegalArgumentException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    protected Map<String, Object> clientBadRequestException(Exception e, WebRequest request) {
        return errorAttributes.getErrorAttributes(request, false);
    }

    @ExceptionHandler(value = {Throwable.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public Map<String, Object> unknownException(Throwable e, WebRequest request) {
        logger.error("Unhandled exception", e);
        Map<String, Object> errorAttributesMap = errorAttributes.getErrorAttributes(request, false);
        errorAttributesMap.put("message", "Internal server error.");
        return errorAttributesMap;
    }

}
