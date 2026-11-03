package fr.asr.calculator.exception.functionnal;

import fr.asr.calculator.exception.ExceptionType;

public class CadenceException extends FunctionalException {

    public CadenceException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public CadenceException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
