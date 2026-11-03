package fr.asr.calculator.exception.functionnal;

import fr.asr.calculator.exception.ExceptionType;

public class ProfileException extends FunctionalException {

    public ProfileException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public ProfileException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
