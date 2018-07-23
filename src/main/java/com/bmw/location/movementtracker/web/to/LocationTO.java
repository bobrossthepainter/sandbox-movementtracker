package com.bmw.location.movementtracker.web.to;

import lombok.Data;

/**
 * Represents a location.
 *
 * @author Robert Lang
 */
@Data
public class LocationTO {

    private Long timestamp;
    private Double latitude;
    private Double longitude;
    private Integer heading;

}
