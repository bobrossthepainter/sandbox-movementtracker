package com.bmw.location.movementtracker.dataaccess;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Testing {@link LocationDAO}.
 *
 * @author Robert Lang
 */
public class SessionDAOTest {

    private static final String VIN_UNKNOWN = "abc";

    @InjectMocks
    private SessionDAO underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void findSessionsForVin_sessionIdAdded() {
        // given
        final String sessionId = underTest.createOrUpdateEntity(
                createBE("123")
        );

        // when
        final List<SessionBE> list = underTest.findSessionsForVin("123");

        // then
        assertEquals(1, list.size());
        assertEquals("123", list.get(0).getVin());
        assertEquals(sessionId, list.get(0).getId());
    }

    @Test
    public void findSessionsForVin_returnNoSessionsForUnknownVin() {
        // given
        underTest.createOrUpdateEntity(
                createBE("123")
        );

        // when
        final List<SessionBE> r = underTest.findSessionsForVin(VIN_UNKNOWN);

        // then
        assertEquals(0, r.size());
    }

    @Test
    public void findSessionsForVin_returnSessionForKnownVin() {
        // given
        final SessionBE sessionBE = new SessionBE();
        sessionBE.setVin("foobar");
        underTest.createOrUpdateEntity(sessionBE);

        // when
        final List<SessionBE> r = underTest.findSessionsForVin("foobar");

        // then
        assertEquals(1, r.size());
    }

    private void createSessions(final String vin, final long... timestamps
    ) {
        for (long t : timestamps) {
            final SessionBE be = new SessionBE();
            be.setCreatedAt(t);
            be.setVin(vin);
            underTest.createOrUpdateEntity(be);
        }
    }

    private SessionBE createBE(final String vin) {
        final SessionBE be = new SessionBE();
        be.setVin(vin);
        return be;
    }
}
