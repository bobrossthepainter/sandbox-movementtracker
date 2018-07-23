package com.bmw.location.movementtracker.dataaccess;


import org.junit.jupiter.api.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testing {@link LocationDAO}.
 *
 * @author Robert Lang
 */
public class AbstractDAOTest {

    private static final List<String> NEW_IDS = asList("a", "b", "c", "d");
    private DummyDAO underTest = new DummyDAO(NEW_IDS);

    @Test
    public void createEntity_returnIdWhenCreated() {
        // given
        final DummyBE be = new DummyBE();

        // when
        final String id = underTest.createOrUpdateEntity(be);

        // then
        assertEquals("a", id);
    }

    @Test
    public void createEntity_returnIdWhenIdGiven() {
        // given
        final DummyBE be = new DummyBE();
        be.id = "f";

        // when
        final String id = underTest.createOrUpdateEntity(be);

        // then
        assertEquals("f", id);
    }

    @Test
    public void createEntity_overwriteWhenIdAlreadyExisting() {
        // given
        final DummyBE be = new DummyBE();
        be.id = "f";
        be.value = "old";
        underTest.createOrUpdateEntity(be);
        final DummyBE beNew = new DummyBE();
        beNew.id = "f";
        beNew.value = "new";

        // when
        underTest.createOrUpdateEntity(beNew);

        // then
        final DummyBE f = underTest.findEntity("f");
        assertEquals("new", f.value);
    }

    @Test
    public void findAll_returnAllEntities() {
        // given
        underTest.createOrUpdateEntity(new DummyBE());
        underTest.createOrUpdateEntity(new DummyBE());

        // when
        final List<DummyBE> all = underTest.findAll();

        // then
        assertEquals(2, all.size());
    }

    @Test
    public void findEntity_returnEntityForId() {
        // given
        final DummyBE be = new DummyBE();
        final String id = underTest.createOrUpdateEntity(be);

        // when
        final DummyBE e = underTest.findEntity("a");

        // then
        assertNotNull(e);
        assertEquals("a", e.id);
    }

    @Test
    public void findEntity_returnNoEntityForUnknownId() {
        // given
        final DummyBE be = new DummyBE();
        final String id = underTest.createOrUpdateEntity(be);

        // when
        final DummyBE r = underTest.findEntity("b");

        // then
        assertNull(r);
    }

    private class DummyBE {
        String id;
        String value;
    }

    private class DummyDAO extends AbstractDAO<DummyBE> {

        private final List<String> ids;
        private int cnt = 0;

        DummyDAO(List<String> ids) {
            this.ids = ids;
        }

        @Override
        protected String getEntityId(final DummyBE entity) {
            return entity.id;
        }

        @Override
        protected String createAndSetNewEntityId(final DummyBE entity) {
            final String id = ids.get(cnt++);
            entity.id = id;
            return id;
        }
    }

}

















