package com.gdsc.game.common.exception;

// job이 존재하지 않음.
public class JobNotFoundException extends RuntimeException {
    public JobNotFoundException(String message) {
        super(message);
    }
}
