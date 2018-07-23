package com.bmw.location.movementtracker.web.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Wrapper object needed for wrapping lists of simple types in Jax-Rs.
 *
 * @author Robert Lang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VehiclesWrapper {

    private List<String> vins;

}
