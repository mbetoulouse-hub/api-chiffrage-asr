package fr.asr.calculator.exception.functionnal;

import fr.asr.calculator.exception.ExceptionType;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public abstract class FunctionalException extends RuntimeException {

    private final ExceptionType exceptionType;
    private final Map<String, Object> debugInfo = new HashMap<>();

    protected FunctionalException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType.name());
        this.exceptionType = exceptionType;
        extractDebugInfo(objects);
    }

    protected FunctionalException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType.name(), throwable);
        this.exceptionType = exceptionType;
        extractDebugInfo(objects);
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
