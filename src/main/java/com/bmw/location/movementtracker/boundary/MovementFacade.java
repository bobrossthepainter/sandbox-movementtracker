package com.bmw.location.movementtracker.boundary;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.bmw.location.movementtracker.business.LocationMapper;
import com.bmw.location.movementtracker.business.LocationService;
import com.bmw.location.movementtracker.business.SessionService;
import com.bmw.location.movementtracker.business.VehicleService;
import com.bmw.location.movementtracker.common.BusinessException;
import com.bmw.location.movementtracker.common.ErrorGroup;
import com.bmw.location.movementtracker.dataaccess.LocationBE;
import com.bmw.location.movementtracker.dataaccess.SessionBE;
import com.bmw.location.movementtracker.dataaccess.VehicleBE;
import com.bmw.location.movementtracker.web.to.LocationTO;

import lombok.extern.slf4j.Slf4j;

/**
 * Facade for movement related requests.
 *
 * @author Robert Lang
 */
@Slf4j
@ApplicationScoped
public class MovementFacade {

    @Inject
    private VehicleService vehicleService;
    @Inject
    private SessionService sessionService;
    @Inject
    private LocationService locationService;
    @Inject
    private LocationMapper locationMapper;

    /**
     * Returns a list of {@link LocationTO} objects for the given session and vin.
     *
     * @param vin     the vehicle id.
     * @param session the session.
     *
     * @return a list of locations.
     */
    public List<LocationTO> getLocationsForSession(final String vin, final String session) {
        log.debug("Get all Location for vin \"{}\" and session \"{}\"", vin, session);
        if (sessionService.getSessionsForVin(vin)
                .stream()
                .noneMatch(it -> session.equals(it.getId()))
                ) {
            return new ArrayList<>();
        }
        return locationService.getLocationsForSession(session)
                .stream()
                .map(locationMapper::mapEntityToTransportObject)
                .collect(Collectors.toList());
    }

    /**
     * Returns the most recent {@link LocationTO} for a given vin.
     *
     * @param vin the vehicle id.
     *
     * @return the most recent location.
     */
    public LocationTO getMostRecentLocationOfVehicle(final String vin) {
        log.debug("Get last Location for vin \"{}\"", vin);
        if (!vehicleService.isExisting(vin)) {
            throw new BusinessException(ErrorGroup.NOT_FOUND, "Vehicle not found.");
        }

        final SessionBE session = sessionService.getMostRecentUpdatedSession(vin)
                .orElseThrow(() -> createNoLocationBusinessException());

        final LocationBE location = locationService.getMostRecentLocationOfSession(session)
                .orElseThrow(() -> createNoLocationBusinessException());

        return locationMapper.mapEntityToTransportObject(location);
    }

    /**
     * Adds a location to an existing or not yet existing movement session. If the vehicle or session aren't existing
     * yet, they will be created and the location will be added to the movement session.
     *
     * @param vin        the vin.
     * @param sessionId  the session id.
     * @param locationTO the location object.
     *
     * @return the id of the created location object.
     */
    public String addLocationToSession(final String vin, final String sessionId, final LocationTO locationTO) {
        log.info("Adding a location of vehicle \"{}\" in session\"{}\"", vin, sessionId);
        if (!vehicleService.isExisting(vin)) {
            vehicleService.createVehicle(vin);
        }

        final Long timestamp = locationTO.getTimestamp();
        sessionService.addOrUpdateSession(vin, sessionId, timestamp);

        final LocationBE locationEntity = locationMapper.mapTransportObjectToEntity(locationTO, sessionId);
        return locationService.addLocationForSession(locationEntity);
    }

    /**
     * Get all available vehicles.
     *
     * @return all vehicles as list.
     */
    public List<String> getAllVehicles() {
        log.debug("Get all vehicle ids");
        return vehicleService.getAllVehicles()
                .stream()
                .map(VehicleBE::getVin)
                .collect(Collectors.toList());
    }

    /**
     * Get all sessions for a specific vehicle.
     *
     * @return all sessions of a vehicle as list.
     */
    public List<String> getSessionsForVin(final String vin) {
        log.info("Get all sessions for vin \"{}\"", vin);
        return sessionService.getSessionsForVin(vin)
                .stream()
                .map(SessionBE::getId)
                .collect(Collectors.toList());
    }

    private BusinessException createNoLocationBusinessException() {
        return new BusinessException(ErrorGroup.NOT_FOUND, "No location of vehicle available.");
    }
}
