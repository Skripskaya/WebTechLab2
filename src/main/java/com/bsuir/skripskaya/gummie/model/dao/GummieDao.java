package com.bsuir.skripskaya.gummie.model.dao;

import com.bsuir.skripskaya.gummie.model.entities.gummie.Gummie;
import com.bsuir.skripskaya.gummie.model.enums.SortField;
import com.bsuir.skripskaya.gummie.model.enums.SortOrder;
import com.bsuir.skripskaya.gummie.model.exceptions.DaoException;

import java.util.List;
import java.util.Optional;

/**
 * @author nekit
 * @version 1.0
 */
public interface GummieDao {
    /**
     * Find gummie by id
     *
     * @param key id of gummie
     * @return gummie with id
     * @throws DaoException throws when there is some errors during dao method execution
     */
    Optional<Gummie> get(Long key) throws DaoException;

    /**
     * Find phones from database
     *
     * @param offset    - offset of found gummies
     * @param limit     - limit of found gummies
     * @param sortField - field to sort
     * @param sortOrder - sort order (asc or desc)
     * @param query     - query for find
     * @return List of gummies
     * @throws DaoException throws when there is some errors during dao method execution
     */

    List<Gummie> findAll(int offset, int limit, SortField sortField, SortOrder sortOrder, String query) throws DaoException;
}
