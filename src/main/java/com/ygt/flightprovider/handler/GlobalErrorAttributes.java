package com.ygt.flightprovider.handler;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.reactive.function.server.ServerRequest;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {

    public GlobalErrorAttributes() {
        super(true);
    }

    @Override
    public Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        Throwable throwable = getError(request);
        errorAttributes.put("message", getMessage(throwable));
        return errorAttributes;
    }

    private String getMessage(Throwable throwable) {
        if (throwable instanceof WebExchangeBindException) {
            WebExchangeBindException e = (WebExchangeBindException) throwable;
            return Objects.nonNull(e.getFieldError()) ?
                e.getFieldError().getField() + " " + e.getFieldError().getDefaultMessage() :
                e.getMessage();
        }
        return throwable.getMessage();
    }

}