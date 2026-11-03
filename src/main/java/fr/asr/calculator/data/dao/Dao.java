package fr.asr.calculator.data.dao;

import java.util.List;
import java.util.Optional;

public interface Dao<T, K> {

    /**
     * Find all entities
     *
     * @return List of entities
     */
    List<T> findAll();

    /**
     * Find entity by id
     *
     * @param id entity id
     * @return Optional of entity
     */
    Optional<T> findByIdOptional(K id);

    /**
     * Find entity by id
     *
     * @param id entity id
     * @return entity
     */
    T findById(K id);

    /**
     * Save entity
     *
     * @param entity entity
     * @return saved entity
     */
    T save(T entity);

    /**
     * Delete entity
     *
     * @param entity entity
     */
    void delete(T entity);

    /**
     * Check if entity exists by id
     *
     * @param id entity id
     * @return true if entity exists, false otherwise
     */
    boolean existsById(K id);
}
