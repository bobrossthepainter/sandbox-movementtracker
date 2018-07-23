package com.bmw.location.movementtracker.dataaccess;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Data access object for {@link LocationBE}.
 *
 * @author Robert Lang
 */
@ApplicationScoped
public class LocationDAO extends AbstractDAO<LocationBE> {

    /**
     * Returns all {@link LocationBE} associated with the given session as a list.  The list is returned in ascending
     * order.
     *
     * @param session the session.
     * @return a list of {@link LocationBE}.
     */
    public List<LocationBE> findLocationsForSession(final String session) {
        return inMemoryDatabase.entrySet().stream()
                .filter(
                        it -> session.equals(it.getValue().getSession())
                )
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    protected String getEntityId(final LocationBE entity) {
        return entity.getId();
    }

    @Override
    protected String createAndSetNewEntityId(final LocationBE entity) {
        final String id = UUID.randomUUID().toString();
        entity.setId(id);
        return id;
    }
}
