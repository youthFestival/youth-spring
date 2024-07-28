package com.youth.server.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BusinessLogicException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public BusinessLogicException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }

    public ExceptionCode getExceptionCode() {
        return exceptionCode;
    }

    public enum ExceptionCode {
        UNABLE_TO_SEND_EMAIL("메일을 보낼 수 없습니다."),
        MEMBER_EXISTS("Member already exists."),
        NO_SUCH_ALGORITHM("No such algorithm.");

        private final String message;

        ExceptionCode(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}