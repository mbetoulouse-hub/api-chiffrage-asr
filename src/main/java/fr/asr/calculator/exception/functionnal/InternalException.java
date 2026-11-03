package fr.asr.calculator.exception.functionnal;

import fr.asr.calculator.exception.ExceptionType;

public class InternalException extends FunctionalException {

    public InternalException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public InternalException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
