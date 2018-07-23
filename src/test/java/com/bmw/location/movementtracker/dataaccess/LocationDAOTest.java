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
public class LocationDAOTest {

    private static final String SESSION_UNKNOWN = "fooNoBar";

    @InjectMocks
    private LocationDAO underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createLocationForSession_sessionIdAdded() {
        // given
        final LocationBE be = createBE("foobar");

        // when
        final String locationId = underTest.createOrUpdateEntity(be);

        // then
        final List<LocationBE> locationList = underTest.findLocationsForSession("foobar");
        assertEquals(1, locationList.size());
        assertEquals("foobar", locationList.get(0).getSession());
        assertEquals(locationId, locationList.get(0).getId());
    }

    @Test
    public void findLocationsForSession_returnNoLocationsForUnknownSession() {
        // given
        // when
        final List<LocationBE> r = underTest.findLocationsForSession(SESSION_UNKNOWN);

        // then
        assertEquals(0, r.size());
    }

    @Test
    public void findLocationsForSession_returnLocationForKnownSession() {
        // given
        final LocationBE locationBE = new LocationBE();
        locationBE.setHeading(1);
        locationBE.setSession("foobar");
        underTest.createOrUpdateEntity(locationBE);

        // when
        final List<LocationBE> r = underTest.findLocationsForSession("foobar");

        // then
        assertEquals(1, r.size());
        assertEquals(1, r.get(0).getHeading().intValue());
    }

    private void createLocations(final String session, final long... timestamps
    ) {
        for (long t : timestamps) {
            final LocationBE be = new LocationBE();
            be.setTimestamp(t);
            be.setSession(session);
            underTest.createOrUpdateEntity(be);
        }
    }

    private LocationBE createBE(final String session) {
        final LocationBE be = new LocationBE();
        be.setSession(session);
        return be;
    }
}
