package com.bmw.location.movementtracker.business;


import com.bmw.location.movementtracker.dataaccess.VehicleBE;
import com.bmw.location.movementtracker.dataaccess.VehicleDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Testing {@link VehicleService}.
 *
 * @author Robert Lang
 */
public class VehicleServiceTest {

    @Mock
    private VehicleDAO vehicleDAO;
    @InjectMocks
    private VehicleService underTest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void isExisting_shouldReturnTrueWhenEntityExisting() {
        // given
        when(vehicleDAO.findEntity(eq("foobar"))).thenReturn(new VehicleBE());

        // when
        final boolean r = underTest.isExisting("foobar");

        // then
        assertTrue(r);
    }

    @Test
    public void isExisting_shouldReturnFalseWhenNoEntityExisting() {
        // given
        when(vehicleDAO.findEntity(eq("foobar"))).thenReturn(null);

        // when
        final boolean r = underTest.isExisting("foobar");

        // then
        assertFalse(r);
    }

    @Test
    public void createVehicle_shouldCreateVehicleWithVin() {
        // given
        // when
        final String id = underTest.createVehicle("foobar");

        // then
        ArgumentCaptor<VehicleBE> sessionCaptor = ArgumentCaptor.forClass(VehicleBE.class);
        verify(vehicleDAO).createOrUpdateEntity(sessionCaptor.capture());
        assertEquals("foobar", sessionCaptor.getValue().getVin());
    }

}

















