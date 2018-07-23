package com.bmw.location.movementtracker.web.interimauth;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import lombok.extern.slf4j.Slf4j;

/**
 * This resource is generating JWT-Tokens. In a production ready and secure system this web-resource should be part of
 * another central authorization service. It is only here for having a small proof of concept in one app server.
 * Furthermore, the resources in this class for creating JWTs are not secured.
 *
 * @author Robert Lang
 * @see {@code https://docs.payara.fish/documentation/microprofile/jwt.html}
 */
@Slf4j
@ApplicationScoped
@Path("interim/auth")
public class AuthResource {

    @GET
    @Path("/allroles")
    @Produces(MediaType.TEXT_PLAIN)
    public Response createJWT() {
        return generateJwtResponse("tmpl_token.json");
    }

    private Response generateJwtResponse(final String jwt) {
        try {
            final String entity = JwtTokenGenerator.generateJWTString(jwt);
            return Response.ok(entity).build();
        } catch (final Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }
}
