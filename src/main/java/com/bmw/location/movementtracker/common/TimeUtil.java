package com.bmw.location.movementtracker.common;


import javax.enterprise.context.ApplicationScoped;

/**
 * Util for Time.
 *
 * @author Robert Lang
 */
@ApplicationScoped
public class TimeUtil {

    /**
     * Returns the current time in seconds.
     *
     * @return time in seconds.
     */
    public long getCurrentTimeInSeconds() {
        return System.currentTimeMillis() / 1000;
    }
}
