package fr.asr.calculator.exception.functionnal;

import fr.asr.calculator.exception.ExceptionType;

public class EstimateException extends FunctionalException {

    public EstimateException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public EstimateException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
