package com.bmw.location.movementtracker.web;

import com.bmw.location.movementtracker.common.BusinessException;
import com.bmw.location.movementtracker.common.ErrorGroup;

import java.text.MessageFormat;

/**
 * Helps with null checks.
 *
 * @author Robert Lang
 */
public final class NullCheckerUtil {

    /**
     * Checks if the given value is null or if it's a non-null string empty. When the check is positive, a {@link
     * BusinessException} is thrown.
     *
     * @param value the checked value.
     * @param id    the id for the exception message.
     */
    public static void checkNotNull(Object value, String id) {
        if (value == null || isOfTypeStringAndEmpty(value)) {
            throw new BusinessException(
                    ErrorGroup.BAD_REQUEST,
                    MessageFormat.format("The argument \"{0}\" can not be undefined.", id)
            );
        }
    }

    private static boolean isOfTypeStringAndEmpty(final Object value) {
        return value instanceof String && ((String) value).isEmpty();
    }
}
