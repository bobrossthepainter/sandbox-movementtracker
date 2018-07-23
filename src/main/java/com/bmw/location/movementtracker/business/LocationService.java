package com.bmw.location.movementtracker.business;

import com.bmw.location.movementtracker.dataaccess.LocationBE;
import com.bmw.location.movementtracker.dataaccess.LocationDAO;
import com.bmw.location.movementtracker.dataaccess.SessionBE;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Business activity offering location related business logic.
 *
 * @author Robert Lang
 */
@ApplicationScoped
public class LocationService {

    @Inject
    private LocationDAO locationDAO;

    /**
     * Returns the most recent location of the given session.
     *
     * @param session the session.
     * @return an optional of {@link LocationBE}.
     */
    public Optional<LocationBE> getMostRecentLocationOfSession(final SessionBE session) {
        final List<LocationBE> locations = locationDAO.findLocationsForSession(session.getId());

        return locations.stream().max((o1, o2) -> o1.getTimestamp() < o2.getTimestamp() ? -1 : 1);
    }

    /**
     * Returns a list of all {@link LocationBE} associated with the given session id. The list is sorted in ascending
     * order in regard of {@link LocationBE#getTimestamp()}.
     *
     * @param sessionId the session id.
     * @return a list of {@link LocationBE}.
     */
    public List<LocationBE> getLocationsForSession(final String sessionId) {
        return locationDAO.findLocationsForSession(sessionId).stream()
                .sorted((o1, o2) -> o1.getTimestamp() < o2.getTimestamp() ? -1 : 1)
                .collect(Collectors.toList());
    }

    /**
     * Adds the given location.
     *
     * @param entity the location.
     * @return the location id.
     */
    public String addLocationForSession(final LocationBE entity) {
        return locationDAO.createOrUpdateEntity(entity);
    }
}
