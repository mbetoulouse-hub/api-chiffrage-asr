package fr.asr.calculator.exception.functionnal;

import fr.asr.calculator.exception.ExceptionType;

public class ExportException extends FunctionalException {

    public ExportException(ExceptionType exceptionType, Object... objects) {
        super(exceptionType, objects);
    }

    public ExportException(ExceptionType exceptionType, Throwable throwable, Object... objects) {
        super(exceptionType, throwable, objects);
    }
}
