package com.orange.devrap.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExceptionMessage{
    LocalDateTime timestamp;
    int statusCode;
    String title;
    String path;
}
