package com.example.schedule_api.exception;

/**
 * 존재하지 않는 리소스 조회 시 던지는 예외. 404 로 매핑된다.
 */
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
