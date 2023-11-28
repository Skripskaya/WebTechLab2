package com.bsuir.skripskaya.gummie.model.dao.impl;

import com.bsuir.skripskaya.gummie.model.dao.StockDao;
import com.bsuir.skripskaya.gummie.model.entities.stock.Stock;
import com.bsuir.skripskaya.gummie.model.entities.stock.StocksExtractor;
import com.bsuir.skripskaya.gummie.model.exceptions.DaoException;
import com.bsuir.skripskaya.gummie.model.utils.ConnectionPool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Using jdbc to work with stock
 *
 * @author skripskaya
 * @version 1.0
 */
public class JdbcStockDao implements StockDao {
    /**
     * Instance of logger
     */
    private static final Logger log = Logger.getLogger(StockDao.class);
    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    /**
     * Stock extractor
     */
    private StocksExtractor stocksExtractor = new StocksExtractor();
    /**
     * Instance of connection pool
     */
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    /**
     * Instance of StockDao
     */
    private static volatile StockDao instance;
    /**
     * SQL query to find stock by id
     */
    private static final String GET_STOCK_BY_ID = "SELECT * FROM stocks WHERE gummieId = ?";
    /**
     * SQL query to update reserved stock
     */
    private static final String UPDATE_STOCK = "UPDATE stocks SET reserved = ? WHERE gummieId = ?";

    /**
     * Realisation of Singleton pattern
     *
     * @return instance of StockDao
     */
    public static StockDao getInstance() {
        if (instance == null) {
            synchronized (StockDao.class) {
                if (instance == null) {
                    instance = new JdbcStockDao();
                }
            }
        }
        return instance;
    }

    /**
     * Find available stock in database
     *
     * @param gummieId id of gummie
     * @return available stock
     * @throws DaoException throws when there is some errors during dao method execution
     */
    @Override
    public Integer availableStock(Long gummieId) throws DaoException {
        Stock stock = getStock(gummieId);
        if (stock != null) {
            return stock.getStock() - stock.getReserved();
        } else {
            return 0;
        }
    }

    /**
     * Update reserve number of gummies in database
     *
     * @param gummieId  - gummie to update
     * @param quantity - quantity to add in reserve field
     * @throws DaoException throws when there is some errors during dao method execution
     */
    @Override
    public void reserve(Long gummieId, int quantity) throws DaoException {
        Stock stock = getStock(gummieId);
        if (stock != null) {
            int newReserved = stock.getReserved() + quantity;
            Connection conn = null;
            PreparedStatement statement = null;
            try {
                lock.writeLock().lock();
                conn = connectionPool.getConnection();
                statement = conn.prepareStatement(UPDATE_STOCK);
                statement.setLong(2, gummieId);
                statement.setLong(1, newReserved);
                statement.execute();
                log.log(Level.INFO, "Update reserve stock in the database");
            } catch (SQLException ex) {
                log.log(Level.ERROR, "Error in reserve", ex);
                throw new DaoException("Error in process of reserving stock");
            } finally {
                lock.writeLock().unlock();
                if (statement != null) {
                    try {
                        statement.close();
                    } catch (SQLException ex) {
                        log.log(Level.ERROR, "Error in closing statement", ex);
                    }
                }
                if (conn != null) {
                    connectionPool.releaseConnection(conn);
                }
            }
        }
    }

    /**
     * Get stock of gummie in database
     *
     * @param phoneId id of gummie
     * @return stock of gummie
     * @throws DaoException throws when there is some errors during dao method execution
     */
    private Stock getStock(Long phoneId) throws DaoException {
        Stock stock = null;
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            conn = connectionPool.getConnection();
            statement = conn.prepareStatement(GET_STOCK_BY_ID);
            statement.setLong(1, phoneId);
            ResultSet resultSet = statement.executeQuery();
            stock = stocksExtractor.extractData(resultSet).get(0);
            log.log(Level.INFO, "Found stock by gummieId in the database");
        } catch (SQLException ex) {
            log.log(Level.ERROR, "Error in getStock", ex);
            throw new DaoException("Error in process of getting stock");
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    log.log(Level.ERROR, "Error in closing statement", ex);
                }
            }
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return stock;
    }
}
