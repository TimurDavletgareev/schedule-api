package ru.ktelabs.schedule_service.error.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConflictOnRequestException extends RuntimeException {

    public ConflictOnRequestException(String message) {
        super(message);
    }
}
