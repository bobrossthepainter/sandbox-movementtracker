package com.bmw.location.movementtracker.business;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import com.bmw.location.movementtracker.common.BusinessException;
import com.bmw.location.movementtracker.common.ErrorGroup;
import com.bmw.location.movementtracker.common.TimeUtil;
import com.bmw.location.movementtracker.dataaccess.SessionBE;
import com.bmw.location.movementtracker.dataaccess.SessionDAO;

import lombok.extern.slf4j.Slf4j;

/**
 * Business activity offering movement session related business logic.
 *
 * @author Robert Lang
 */
@Slf4j
@ApplicationScoped
public class SessionService {

    @Inject
    private VehicleService vehicleService;
    @Inject
    private SessionDAO sessionDAO;
    @Inject
    private TimeUtil timeUtil;

    /**
     * Updates or creates a session for the given id and vin.
     *
     * @param vin       the vin.
     * @param sessionId the session id.
     *
     * @return the session.
     */
    public void addOrUpdateSession(final String vin, final String sessionId, final Long timestamp) {
        final SessionBE entity = sessionDAO.findEntity(sessionId);
        checkVinAssociatonMatching(vin, entity);

        if (entity != null) {
            log.debug("Updating existing session \"{}\"", sessionId);
            updateSessionTimestamp(entity, timestamp);
        } else {
            log.debug("Creating new session \"{}\"", sessionId);
            createSession(vin, sessionId, timestamp);
        }
    }

    /**
     * Returns a list of session for the given vehicle id. The list ist ordered ascending related to {@link
     * SessionBE#getUpdatedAt()}
     *
     * @param vin the vehicle identifier.
     *
     * @return a list of associated sessions.
     */
    public List<SessionBE> getSessionsForVin(final String vin) {
        return sessionDAO.findSessionsForVin(vin).stream()
                .sorted((o1, o2) -> o1.getUpdatedAt() < o2.getUpdatedAt() ? -1 : 1)
                .collect(Collectors.toList());
    }

    private void checkVinAssociatonMatching(final String givenVin, final SessionBE session) {
        if (session != null && !givenVin.equals(session.getVin())) {
            throw new BusinessException(
                    ErrorGroup.BAD_REQUEST,
                    "Movement session has already been associated with another vehicle."
            );
        }
    }

    private String createSession(final String vin, final String sessionId, final Long timestamp) {
        final SessionBE be = new SessionBE(sessionId, vin, timestamp, timestamp);
        return sessionDAO.createOrUpdateEntity(be);
    }

    private void updateSessionTimestamp(final SessionBE session, final Long timestamp) {
        session.setUpdatedAt(timestamp);
        sessionDAO.createOrUpdateEntity(session);
    }

    public Optional<SessionBE> getMostRecentUpdatedSession(final String vin) {
        return sessionDAO.findSessionsForVin(vin).stream()
                .max((o1, o2) -> o1.getUpdatedAt() < o2.getUpdatedAt() ? -1 : 1);
    }
}
