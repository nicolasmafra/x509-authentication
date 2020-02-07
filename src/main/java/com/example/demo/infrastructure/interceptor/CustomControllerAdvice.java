package com.example.demo.infrastructure.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CustomControllerAdvice extends RequestBodyAdviceAdapter {
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        String strHeaders = inputMessage.getHeaders().entrySet().stream()
                .map(entry -> entry.getKey() + ": " + String.join(";", entry.getValue()))
                .collect(Collectors.joining("\r\n"));
        log.debug("HTTP request:\r\n{}\r\n\r\n{}", strHeaders, body);
        return body;
    }
}
