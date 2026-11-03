package fr.asr.calculator.exception.functionnal;

import fr.asr.calculator.exception.ExceptionType;

public class CalculateException extends FunctionalException {

    public CalculateException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public CalculateException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
