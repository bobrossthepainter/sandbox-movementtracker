package com.bmw.location.movementtracker.web;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bmw.location.movementtracker.boundary.MovementFacade;
import com.bmw.location.movementtracker.web.to.LocationTO;
import com.bmw.location.movementtracker.web.to.SessionsWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * Handling movement related service calls.
 *
 * @author Robert Lang
 */
@ApplicationScoped
@Api(value = "Handling movement related service calls of a specific vehicle")
@Path("/v1/vins/{vin}")
public class MovementSessionResource {

    @Inject
    private MovementFacade movementFacade;

    @GET
    @Path("/position")
    //@RolesAllowed("nsa")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Returns the last known location of a vehicle, derived from the last updated session"
    )
    public LocationTO getLastPosition(
            @PathParam("vin")
            @ApiParam(
                    value = "Vehicle identifier",
                    required = true
            )
            final String vin
    ) {
        NullCheckerUtil.checkNotNull(vin, "vehicle identifier (vin)");

        return movementFacade.getMostRecentLocationOfVehicle(vin);
    }

    @GET
    @Path("/sessions")
    //@RolesAllowed("vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Finds all movement sessions of the vehicle",
            notes = "The sessions are sorted ascending regarding their last location timestamp",
            response = String.class,
            responseContainer = "List"
    )
    public Response getAllSessions(
            @PathParam("vin")
            @ApiParam(
                    value = "Vehicle identifier",
                    required = true
            )
            final String vin
    ) {
        NullCheckerUtil.checkNotNull(vin, "vehicle identifier (vin)");

        final List<String> list = movementFacade.getSessionsForVin(vin);

        return Response.ok(new SessionsWrapper(list)).build();
    }

    @POST
    @Path("/sessions/{session}/locations")
    //@RolesAllowed("vehicle")
    @ApiOperation(
            value = "Creates a location in the movement session of an vehicle"
    )
    public Response addLocation(
            @PathParam("vin")
            @ApiParam(
                    value = "Vehicle identifier",
                    required = true
            )
            final String vin,
            @PathParam("session")
            @ApiParam(
                    value = "Identifier of a vehicle movement session",
                    required = true
            )
            final String session,
            final LocationTO location
    ) {
        checkVinAndSession(vin, session);

        movementFacade.addLocationToSession(vin, session, location);

        return Response.noContent().build();
    }

    private void checkVinAndSession(String vin, String session) {
        NullCheckerUtil.checkNotNull(vin, "vehicle identifier (vin)");
        NullCheckerUtil.checkNotNull(session, "session identifier");
    }

    @GET
    @Path("/sessions/{session}/locations")
    //@RolesAllowed("vehicle")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Finds all locations of a vehicle movement session",
            notes = "The locations are sorted ascending regarding their timestamp",
            response = LocationTO.class,
            responseContainer = "List"
    )
    public Response getLocations(
            @PathParam("vin")
            @ApiParam(
                    value = "Vehicle identifier",
                    required = true
            )
            final String vin,
            @PathParam("session")
            @ApiParam(
                    value = "Identifier of a vehicle movement session",
                    required = true
            )
            final String session
    ) {
        checkVinAndSession(vin, session);

        final List<LocationTO> list = movementFacade.getLocationsForSession(vin, session);
        GenericEntity<List<LocationTO>> wrapped = new GenericEntity<List<LocationTO>>(list) {
        };
        return Response.ok(wrapped).build();
    }
}
