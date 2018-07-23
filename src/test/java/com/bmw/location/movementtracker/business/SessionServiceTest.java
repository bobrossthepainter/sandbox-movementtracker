package com.bmw.location.movementtracker.business;


import com.bmw.location.movementtracker.common.BusinessException;
import com.bmw.location.movementtracker.dataaccess.SessionBE;
import com.bmw.location.movementtracker.dataaccess.SessionDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing {@link SessionService}.
 *
 * @author Robert Lang
 */
public class SessionServiceTest {

    @Mock
    private VehicleService vehicleServiceMock;
    @Mock
    private SessionDAO sessionDaoMock;
    @InjectMocks
    private SessionService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getSessionsForVin_shouldReturnSortedSessionIds() {
        // given
        when(sessionDaoMock.findSessionsForVin(eq("foobar"))).thenReturn(asList(
                new SessionBE("2", "foobar", 1L, 2L),
                new SessionBE("3", "foobar", 3L, 4L),
                new SessionBE("1", "foobar", 3L, 1L)
        ));

        // when
        final List<SessionBE> result = underTest.getSessionsForVin("foobar");

        // then
        assertEquals(3, result.size());
        assertEquals("1", result.get(0).getId());
        assertEquals("2", result.get(1).getId());
        assertEquals("3", result.get(2).getId());
    }

    @Test
    public void addOrUpdateSession_shouldCreateSessionIfNotExisting() {
        // given
        // when
        underTest.addOrUpdateSession("foobar", "ladida", 4711L);

        // then
        verify(sessionDaoMock).findEntity(eq("ladida"));
        ArgumentCaptor<SessionBE> sessionCaptor = ArgumentCaptor.forClass(SessionBE.class);
        verify(sessionDaoMock).createOrUpdateEntity(sessionCaptor.capture());
        assertEquals("ladida", sessionCaptor.getValue().getId());
        assertEquals("foobar", sessionCaptor.getValue().getVin());
        assertEquals(4711L, sessionCaptor.getValue().getCreatedAt().longValue());
        assertEquals(4711L, sessionCaptor.getValue().getUpdatedAt().longValue());
    }

    @Test
    public void addOrUpdateSession_shouldThrowBusinessExceptionWhenSessionAssociatedWithAnotherVin() {
        // given
        when(vehicleServiceMock.isExisting(eq("foobar"))).thenReturn(true);
        final SessionBE be = createSession("vinAnother", "ladida", 1L, 1L);
        when(sessionDaoMock.findEntity(eq("ladida"))).thenReturn(be);

        // when
        final BusinessException e = assertThrows(
                BusinessException.class,
                () -> underTest.addOrUpdateSession("foobar", "ladida", null)
        );

        // then
        assertEquals("Movement session has already been associated with another vehicle.", e.getMessage());
    }

    @Test
    public void addOrUpdateSession_updateTimestampWhenSessionExisting() {
        // given
        when(vehicleServiceMock.isExisting(eq("foobar"))).thenReturn(true);
        final SessionBE be = createSession("foobar", "ladida", 1L, 1L);
        when(sessionDaoMock.findEntity(eq("ladida"))).thenReturn(be);

        // when
        underTest.addOrUpdateSession("foobar", "ladida", 4711L);

        // then
        ArgumentCaptor<SessionBE> sessionCaptor = ArgumentCaptor.forClass(SessionBE.class);
        verify(sessionDaoMock).createOrUpdateEntity(sessionCaptor.capture());
        assertEquals(4711L, sessionCaptor.getValue().getUpdatedAt().longValue());
    }

    private SessionBE createSession(
            final String vin,
            final String sessionId,
            final Long createdTimestamp,
            final Long updateTimestamp) {
        return new SessionBE(sessionId, vin, createdTimestamp, updateTimestamp);
    }
}

















