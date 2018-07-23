package com.bmw.location.movementtracker.common;

/**
 * Exception for handling business errors.
 *
 * @author Robert Lang
 */
public class BusinessException extends RuntimeException {

    private final ErrorGroup errorGroup;

    public BusinessException(final ErrorGroup errorGroup, final String message) {
        super(message);
        this.errorGroup = errorGroup;
    }

    public ErrorGroup getErrorGroup() {
        return errorGroup;
    }
}
