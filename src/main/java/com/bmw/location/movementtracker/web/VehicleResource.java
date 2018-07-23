package com.bmw.location.movementtracker.web;

import java.security.Principal;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.bmw.location.movementtracker.boundary.MovementFacade;
import com.bmw.location.movementtracker.web.to.VehiclesWrapper;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * Handling vehicle related service calls.
 *
 * @author Robert Lang
 */
@ApplicationScoped
@Api(description = "Handling static location related service calls regarding a specific vehicle")
@Produces("application/json")
@RolesAllowed("vehicle")
@Path("/v1/vins")
public class VehicleResource {
    @Inject
    private Principal principal;
    @Inject
    private MovementFacade movementFacade;

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(
            value = "Returns a list of vehicle ids",
            response = String.class,
            responseContainer = "List"
    )
    public Response getAllVehicles() {
        final List<String> list = movementFacade.getAllVehicles();

        return Response.ok(new VehiclesWrapper(list)).build();
    }
}