package com.bmw.location.movementtracker.dataaccess;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Data access object for {@link SessionBE}.
 *
 * @author Robert Lang
 */
@ApplicationScoped
public class SessionDAO extends AbstractDAO<SessionBE> {

    /**
     * Returns all {@link SessionBE} associated with the given vin as a list. The list is returned in ascending order.
     *
     * @param vin the vehicle identifier.
     * @return a list of {@link SessionBE}.
     */
    public List<SessionBE> findSessionsForVin(final String vin) {
        return inMemoryDatabase.entrySet().stream()
                .filter(
                        it -> vin.equals(it.getValue().getVin())
                )
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    @Override
    protected String getEntityId(final SessionBE entity) {
        return entity.getId();
    }

    @Override
    protected String createAndSetNewEntityId(final SessionBE entity) {
        final String id = UUID.randomUUID().toString();
        entity.setId(id);
        return id;
    }
}
