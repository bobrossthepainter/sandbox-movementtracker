package com.bmw.location.movementtracker.boundary;


import com.bmw.location.movementtracker.business.*;
import com.bmw.location.movementtracker.dataaccess.LocationBE;
import com.bmw.location.movementtracker.dataaccess.SessionBE;
import com.bmw.location.movementtracker.dataaccess.VehicleBE;
import com.bmw.location.movementtracker.web.to.LocationTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testing {@link MovementFacade}.
 *
 * @author Robert Lang
 */
public class MovementFacadeTest {

    @Mock
    private VehicleService vehicleServiceMock;
    @Mock
    private SessionService sessionServiceMock;
    @Mock
    private LocationService locationServiceMock;
    @Mock
    private LocationMapper locationMapper;
    @InjectMocks
    private MovementFacade underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        final LocationMapperImpl locationMapperImpl = new LocationMapperImpl();
        doAnswer(
                call -> locationMapperImpl.mapEntityToTransportObject(call.getArgument(0))
        ).when(locationMapper).mapEntityToTransportObject(any());
        doAnswer(
                call -> locationMapperImpl.mapTransportObjectToEntity(call.getArgument(0), call.getArgument(1))
        ).when(locationMapper).mapTransportObjectToEntity(any(), any());
    }

    @Test
    public void addLocationToSession_shouldCreateSession() {
        // given
        when(vehicleServiceMock.isExisting(any())).thenReturn(true);

        // when
        underTest.addLocationToSession("foobar", "ladida", createLocationTo(4711L));

        // then
        verify(sessionServiceMock).addOrUpdateSession(eq("foobar"), eq("ladida"), eq(new Long(4711L)));
    }

    @Test
    public void addLocationToSession_shouldCreateVehicleWhenNotExisting() {
        // given
        when(vehicleServiceMock.isExisting(any())).thenReturn(false);

        // when
        underTest.addLocationToSession("foobar", "ladida", createLocationTo(4711L));

        // then
        verify(vehicleServiceMock).createVehicle(eq("foobar"));
    }

    @Test
    public void addLocationToSession_shouldNotCreateVehicleWhenExisting() {
        // given
        when(vehicleServiceMock.isExisting(any())).thenReturn(true);

        // when
        underTest.addLocationToSession("foobar", "ladida", createLocationTo(4711L));

        // then
        verify(vehicleServiceMock, times(0)).createVehicle(eq("foobar"));
    }

    @Test
    public void addLocationToSession_shouldAddLocation() {
        // given
        when(vehicleServiceMock.isExisting(any())).thenReturn(false);

        // when
        underTest.addLocationToSession("foobar", "ladida", createLocationTo(4711L));

        // then
        ArgumentCaptor<LocationBE> captor = ArgumentCaptor.forClass(LocationBE.class);
        verify(locationServiceMock).addLocationForSession(captor.capture());
        assertEquals(4711L, captor.getValue().getTimestamp().longValue());
        assertEquals("ladida", captor.getValue().getSession());
    }

    @Test
    public void getLocationsForSession_shouldReturnTwoLocations() {
        // given
        when(sessionServiceMock.getSessionsForVin("foobar")).thenReturn(
                asList(
                        createSession("foobar", "ladida")
                )
        );
        when(locationServiceMock.getLocationsForSession(eq("ladida"))).thenReturn(
                asList(
                        createLocationEntity("ladida", 1L),
                        createLocationEntity("ladida", 2L)
                )
        );

        // when
        final List<LocationTO> list = underTest.getLocationsForSession("foobar", "ladida");

        // then
        assertEquals(2, list.size());
    }

    @Test
    public void getLocationsForSession_shouldReturnNoLocationsWhenSessionForVinNotExisting() {
        // given
        when(sessionServiceMock.getSessionsForVin("foobar")).thenReturn(
                asList(
                        createSession("foobar", "ladida2")
                )
        );
        when(locationServiceMock.getLocationsForSession(eq("ladida"))).thenReturn(
                asList(
                        createLocationEntity("ladida", 1L),
                        createLocationEntity("ladida", 2L)
                )
        );

        // when
        final List<LocationTO> list = underTest.getLocationsForSession("foobar", "ladida");

        // then
        assertEquals(0, list.size());
    }

    @Test
    public void getLocationsForSession_shouldDoCorrectLocationMapping() {
        // given
        when(sessionServiceMock.getSessionsForVin(eq("foobar"))).thenReturn(
                asList(createSession("foobar", "ladida"))
        );
        final LocationBE be = new LocationBE();
        be.setSession("ladida");
        be.setId("123");
        be.setHeading(1);
        be.setTimestamp(123L);
        be.setLatitude(1.2);
        be.setLongitude(2.2);
        when(locationServiceMock.getLocationsForSession(eq("ladida"))).thenReturn(asList(be));

        // when
        final List<LocationTO> list = underTest.getLocationsForSession("foobar", "ladida");

        // then
        assertEquals(1, list.size());
        final LocationTO to = list.get(0);
        assertEquals(1.2, to.getLatitude().doubleValue());
        assertEquals(2.2, to.getLongitude().doubleValue());
        assertEquals(123L, to.getTimestamp().longValue());
        assertEquals(1, to.getHeading().intValue());
    }

    @Test
    public void getAllVehicles_returnMappedVehicles() {
        // given
        when(vehicleServiceMock.getAllVehicles()).thenReturn(
                asList(
                        createVehicle("foobar"),
                        createVehicle("lolcats")
                )
        );

        // when
        final List<String> list = underTest.getAllVehicles();

        // then
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(it -> "foobar".equals(it)));
        assertTrue(list.stream().anyMatch(it -> "lolcats".equals(it)));
    }

    @Test
    public void getSessionsForVin_returnMappedSessions() {
        // given
        when(sessionServiceMock.getSessionsForVin(eq("ironman"))).thenReturn(
                asList(
                        createSession("ironman", "foobar"),
                        createSession("ironman", "lolcats")
                )
        );

        // when
        final List<String> list = underTest.getSessionsForVin("ironman");

        // then
        assertEquals(2, list.size());
        assertTrue(list.stream().anyMatch(it -> "foobar".equals(it)));
        assertTrue(list.stream().anyMatch(it -> "lolcats".equals(it)));
    }

    private LocationTO createLocationTo(final long timestamp) {
        final LocationTO locationTO = new LocationTO();
        locationTO.setTimestamp(timestamp);
        return locationTO;
    }

    private SessionBE createSession(
            final String vin,
            final String sessionId) {
        return new SessionBE(sessionId, vin, null, null);
    }

    private VehicleBE createVehicle(final String vin) {
        final VehicleBE be = new VehicleBE();
        be.setVin(vin);
        return be;
    }

    private LocationBE createLocationEntity(String session, Long timestamp) {
        final LocationBE be = new LocationBE();
        be.setSession(session);
        be.setTimestamp(timestamp);
        return be;
    }

}

















