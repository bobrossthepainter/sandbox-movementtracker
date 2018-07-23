package com.bmw.location.movementtracker.web;

import com.bmw.location.movementtracker.common.BusinessException;
import com.bmw.location.movementtracker.common.ErrorGroup;
import com.bmw.location.movementtracker.web.to.ExceptionTO;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * Maps exceptions for response representation.
 *
 * @author Robert Lang
 */
@Slf4j
@Provider
public class ResponseExceptionMapper implements ExceptionMapper<BusinessException> {

    @Override
    public Response toResponse(BusinessException e) {
        return Response
                .status(mapResponseStatus(e.getErrorGroup()))
                .entity(createEntity(e.getMessage()))
                .build();
    }

    private Response.Status mapResponseStatus(final ErrorGroup e) {
        if (ErrorGroup.BAD_REQUEST.equals(e)) {
            return Response.Status.BAD_REQUEST;
        } else {
            return Response.Status.NOT_FOUND;
        }
    }

    private ExceptionTO createEntity(final String message) {
        return new ExceptionTO(message);
    }

}

