package com.bsuir.skripskaya.gummie.model.dao;

import com.bsuir.skripskaya.gummie.model.exceptions.DaoException;

/**
 * @author skripskaya
 * @version 1.0
 */
public interface StockDao {
    /**
     * Find available stock of gummie
     *
     * @param phoneId id of gummie
     * @return available stock
     * @throws DaoException throws when there is some errors during dao method execution
     */
    Integer availableStock(Long phoneId) throws DaoException;

    /**
     * Update reserve of gummies in database
     *
     * @param gummieId  - gummie to update
     * @param quantity - quantity to add in reserve field
     * @throws DaoException throws when there is some errors during dao method execution
     */
    void reserve(Long gummieId, int quantity) throws DaoException;
}
