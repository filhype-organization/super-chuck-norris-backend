package app.configuration;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ExceptionMessage{
    LocalDateTime timestamp;
    int statusCode;
    String message;
    String path;
    String method;
}
