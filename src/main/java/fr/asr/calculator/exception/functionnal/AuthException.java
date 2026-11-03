package fr.asr.calculator.exception.functionnal;

import fr.asr.calculator.exception.ExceptionType;

public class AuthException extends FunctionalException {

    public AuthException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public AuthException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
