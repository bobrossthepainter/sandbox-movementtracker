package com.bmw.location.movementtracker.dataaccess;

import lombok.Data;

/**
 * Business entity encapsulating location data.
 *
 * @author Robert Lang
 */
@Data
public class LocationBE {

    private String id;
    private Long timestamp;
    private Double latitude;
    private Double longitude;
    private Integer heading;
    private String session;

}
