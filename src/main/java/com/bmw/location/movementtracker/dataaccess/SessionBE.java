package com.bmw.location.movementtracker.dataaccess;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Business entity encapsulating session data.
 *
 * @author Robert Lang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SessionBE {

    private String id;
    private String vin;
    private Long createdAt;
    private Long updatedAt;

}
