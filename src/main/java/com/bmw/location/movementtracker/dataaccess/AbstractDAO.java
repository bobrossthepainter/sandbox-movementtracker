package com.bmw.location.movementtracker.dataaccess;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract data access object holding the common generic functions.
 *
 * @author Robert Lang
 */
@Slf4j
public abstract class AbstractDAO<T> {

    protected Map<String, T> inMemoryDatabase = new HashMap<>();

    /**
     * Finds a {@link T} and returns it if existing.
     *
     * @param id the entity id.
     * @return the {@link T} or null if not existing.
     */
    public T findEntity(final String id) {
        return inMemoryDatabase.get(id);
    }

    /**
     * Finds all {@link T} and returns it if existing.
     *
     * @return the {@link T} or null if not existing.
     */
    public List<T> findAll() {
        return new ArrayList<>(inMemoryDatabase.values());
    }

    /**
     * Creates or updates a {@link T} and returns its id.
     *
     * @param entity the entity.
     * @return the id of the entity object.
     */
    public String createOrUpdateEntity(final T entity) {
        String id = getEntityId(entity);
        if (id == null) {
            log.debug("Creating new entity");
            id = createAndSetNewEntityId(entity);
        }
        inMemoryDatabase.put(id, entity);
        log.debug("Entity created");
        return id;
    }

    /**
     * Returns the unique id of the given entity.
     *
     * @param entity the entity.
     * @return the unique id.
     */
    protected abstract String getEntityId(final T entity);

    /**
     * Creates and sets an unique entity id for the given entity.
     *
     * @param entity the entity.
     * @return the entity id.
     */
    protected abstract String createAndSetNewEntityId(final T entity);

}
