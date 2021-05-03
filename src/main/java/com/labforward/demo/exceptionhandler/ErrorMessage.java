package com.labforward.demo.exceptionhandler;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
public class ErrorMessage {

    private final int statusCode;
    private final Date timestamp;
    private final String message;
    private final String description;
    private Map<String, String> errors;
}
