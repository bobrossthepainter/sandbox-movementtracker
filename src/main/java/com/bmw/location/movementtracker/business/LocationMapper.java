package com.bmw.location.movementtracker.business;

import com.bmw.location.movementtracker.dataaccess.LocationBE;
import com.bmw.location.movementtracker.web.to.LocationTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper between {@link LocationBE} and {@link LocationTO}
 *
 * @author Robert Lang
 */
@Mapper(componentModel = "cdi")
public interface LocationMapper {

    LocationTO mapEntityToTransportObject(final LocationBE locationBE);

    @Mapping(source = "session", target = "session")
    @Mapping(source = "location.heading", target = "heading")
    @Mapping(source = "location.latitude", target = "latitude")
    @Mapping(source = "location.longitude", target = "longitude")
    @Mapping(source = "location.timestamp", target = "timestamp")
    @Mapping(target = "id", ignore = true)
    LocationBE mapTransportObjectToEntity(final LocationTO location, final String session);

}
