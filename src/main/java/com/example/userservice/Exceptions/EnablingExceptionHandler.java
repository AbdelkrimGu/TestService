package com.example.userservice.Exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class EnablingExceptionHandler implements AuthenticationEntryPoint {

    private final HttpStatus httpStatus;

    public EnablingExceptionHandler(HttpStatus httpStatus) {
        Assert.notNull(httpStatus, "httpStatus cannot be null");
        this.httpStatus = httpStatus;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setStatus(this.httpStatus.value());
        Map<String, Object> json = new HashMap<>();
        json.put("message", "User not enabled");

        // Convert the JSON object to a string
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonString = objectMapper.writeValueAsString(json);

        // Set the content type of the response
        response.setContentType("application/json");

        // Get the output stream and write the JSON string to it
        OutputStream outputStream = response.getOutputStream();
        outputStream.write(jsonString.getBytes());
        outputStream.flush();
    }
}
