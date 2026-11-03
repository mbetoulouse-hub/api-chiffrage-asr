package fr.asr.calculator.exception.functionnal;

import fr.asr.calculator.exception.ExceptionType;

public class NotFoundException extends FunctionalException {

    public NotFoundException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public NotFoundException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
