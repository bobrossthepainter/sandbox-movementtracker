package com.bmw.location.movementtracker.business;

import com.bmw.location.movementtracker.dataaccess.VehicleBE;
import com.bmw.location.movementtracker.dataaccess.VehicleDAO;
import lombok.extern.slf4j.Slf4j;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Business activity offering movement session related business logic.
 *
 * @author Robert Lang
 */
@Slf4j
@ApplicationScoped
public class VehicleService {

    @Inject
    private VehicleDAO vehicleDAO;

    /**
     * Creates a vehicle for the given id.
     *
     * @param vin the vehicle id.
     * @return the vehicle id.
     */
    public String createVehicle(final String vin) {
        log.debug("Creating vehicle with id \"{}\"", vin);
        final VehicleBE vehicleBE = new VehicleBE();
        vehicleBE.setVin(vin);
        return vehicleDAO.createOrUpdateEntity(vehicleBE);
    }

    /**
     * Returns true if a vehicle with the given id already exists.
     *
     * @param vin the id.
     * @return true if exists, false otherwise.
     */
    public boolean isExisting(final String vin) {
        return vehicleDAO.findEntity(vin) != null;
    }

    /**
     * Returns a list of all known vehicles.
     *
     * @return the vin list.
     */
    public List<VehicleBE> getAllVehicles() {
        log.info("Getting all vehicles");
        return vehicleDAO.findAll().stream()
                .collect(Collectors.toList());
    }
}
