package com.bmw.location.movementtracker.web.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Transport object for exception info.
 *
 * @author Robert Lang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionTO {

    private String message;
}
