package com.bmw.location.movementtracker.business;


import com.bmw.location.movementtracker.dataaccess.LocationBE;
import com.bmw.location.movementtracker.dataaccess.LocationDAO;
import com.bmw.location.movementtracker.dataaccess.SessionBE;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing {@link LocationService}.
 *
 * @author Robert Lang
 */
public class LocationServiceTest {

    private static final String DEFAULT_SESSION_ID = "foobar";

    @Mock
    private LocationDAO locationDaoMock;
    @InjectMocks
    private LocationService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getMostRecentLocationOfSession_shouldReturnMostRecentLocation() {
        // given
        arrangeDaoMock();

        // when
        final Optional<LocationBE> result = underTest.getMostRecentLocationOfSession(
                createSession(DEFAULT_SESSION_ID)
        );

        // then
        assertTrue(result.isPresent());
        assertEquals(4L, result.get().getTimestamp().longValue());
    }

    @Test
    public void getLocationsForSession_shouldReturnLocationsinAscendingOrder() {
        // given
        arrangeDaoMock();

        // when
        final List<LocationBE> list = underTest.getLocationsForSession(DEFAULT_SESSION_ID);

        // then
        assertEquals(4, list.size());
        assertEquals(1L, list.get(0).getTimestamp().longValue());
        assertEquals(2L, list.get(1).getTimestamp().longValue());
        assertEquals(3L, list.get(2).getTimestamp().longValue());
        assertEquals(4L, list.get(3).getTimestamp().longValue());
    }

    @Test
    public void addLocationForSession_shouldCreateEntity() {
        // given
        // when
        underTest.addLocationForSession(new LocationBE());

        // then
        verify(locationDaoMock).createOrUpdateEntity(any());
    }

    private void arrangeDaoMock() {
        when(locationDaoMock.findLocationsForSession(eq(DEFAULT_SESSION_ID))).thenReturn(
                asList(
                        createLocationEntity(3L),
                        createLocationEntity(4L),
                        createLocationEntity(1L),
                        createLocationEntity(2L)
                )
        );
    }

    private SessionBE createSession(final String sessionId) {
        return new SessionBE(sessionId, null, null, null);
    }

    private LocationBE createLocationEntity(Long timestamp) {
        final LocationBE be = new LocationBE();
        be.setTimestamp(timestamp);
        return be;
    }

}

















