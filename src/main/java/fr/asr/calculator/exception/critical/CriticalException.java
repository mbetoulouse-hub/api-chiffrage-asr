package fr.asr.calculator.exception.critical;

import fr.asr.calculator.exception.ExceptionType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class CriticalException extends Exception {

    private final ExceptionType exceptionType;
    private final Map<String, Object> debugInfo = new HashMap<>();

    public CriticalException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType.name());
        this.exceptionType = exceptionType;
        extractDebugInfo(objects);
    }

    public CriticalException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType.name(), throwable);
        extractDebugInfo(objects);
        this.exceptionType = exceptionType;
    }

    private void extractDebugInfo(Object... objects) {
        if (objects.length % 2 != 0) {
            throw new RuntimeException(
                    "Debug option invalid (not a key-value pair) %s".formatted(Arrays.toString(objects)));
        }

        for (int i = 0; i < objects.length; i += 2) {
            if (objects[i] instanceof String key) {
                debugInfo.put(key, objects[i + 1]);
            }
        }
    }
}
