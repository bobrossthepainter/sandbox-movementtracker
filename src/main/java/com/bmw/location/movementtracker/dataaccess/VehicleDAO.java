package com.bmw.location.movementtracker.dataaccess;

import javax.enterprise.context.ApplicationScoped;
import java.util.UUID;

/**
 * Data access object for {@link VehicleBE}.
 *
 * @author Robert Lang
 */
@ApplicationScoped
public class VehicleDAO extends AbstractDAO<VehicleBE> {

    @Override
    protected String getEntityId(final VehicleBE entity) {
        return entity.getVin();
    }

    @Override
    protected String createAndSetNewEntityId(final VehicleBE entity) {
        final String id = UUID.randomUUID().toString();
        entity.setVin(id);
        return id;
    }
}
