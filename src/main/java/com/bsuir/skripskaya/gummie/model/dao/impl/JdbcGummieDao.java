package com.bsuir.skripskaya.gummie.model.dao.impl;

import com.bsuir.skripskaya.gummie.model.dao.GummieDao;
import com.bsuir.skripskaya.gummie.model.entities.gummie.Gummie;
import com.bsuir.skripskaya.gummie.model.entities.gummie.GummieExtractor;
import com.bsuir.skripskaya.gummie.model.enums.SortField;
import com.bsuir.skripskaya.gummie.model.enums.SortOrder;
import com.bsuir.skripskaya.gummie.model.exceptions.DaoException;
import com.bsuir.skripskaya.gummie.model.utils.ConnectionPool;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Using jdbc to work with gummies
 *
 * @author skripskaya
 * @version 1.0
 */
public class JdbcGummieDao implements GummieDao {
    /**
     * Instance of logger
     */
    private static final Logger log = Logger.getLogger(GummieDao.class);
    /**
     * Instance of gummieExtractor
     */
    private GummieExtractor gummieExtractor = new GummieExtractor();
    /**
     * Instance of GummieDao
     */
    private static volatile GummieDao instance;
    /**
     * Instance of ConnectionPool
     */
    private ConnectionPool connectionPool = ConnectionPool.getInstance();
    //удалить
    /**
     * SQL query to find gummies by id
     */
    private static final String GET_QUERY = "SELECT * FROM gummie WHERE id = ?";
    //удалить
    /**
     * SQL query to find all gummies with available stock > 0, limit and offset
     */
    private static final String SIMPLE_FIND_ALL_QUERY = "select ph.* " +
            "from (select PHONES.* from PHONES " +
            "left join STOCKS on PHONES.ID = STOCKS.PHONEID where STOCKS.STOCK - STOCKS.RESERVED > 0 and phones.price > 0 offset ? limit ?) ph";
    //удалить
    /**
     * SQL query to find all phones with available stock
     */
    private static final String FIND_WITHOUT_OFFSET_AND_LIMIT = "SELECT ph.* " +
            "FROM (SELECT phones.* FROM phones " +
            "LEFT JOIN stocks ON phones.id = stocks.phoneId WHERE stocks.stock - stocks.reserved > 0 ";

    private final ReadWriteLock lock = new ReentrantReadWriteLock();
    //вопрос
    /**
     * SQL query to find number of phones
     */
    private static final String NUMBER_OF_PHONES_QUERY = "SELECT count(*) FROM PHONES LEFT JOIN STOCKS ON PHONES.ID = STOCKS.PHONEID WHERE STOCKS.STOCK - STOCKS.RESERVED > 0 AND phones.price > 0";

    /**
     * Realisation of Singleton pattern
     *
     * @return instance of GummieDao
     */
    public static GummieDao getInstance() {
        if (instance == null) {
            synchronized (GummieDao.class) {
                if (instance == null) {
                    instance = new JdbcGummieDao();
                }
            }
        }
        return instance;
    }

    /**
     * Get gummie by id from database
     *
     * @param key id of gummie
     * @return gummie
     * @throws DaoException throws when there is some errors during dao method execution
     */
    @Override
    public Optional<Gummie> get(Long key) throws DaoException {
        Optional<Gummie> gummie;
        Connection conn = null;
        PreparedStatement statement = null;
        try {
            lock.readLock().lock();
            conn = connectionPool.getConnection();
            statement = conn.prepareStatement(GET_QUERY);
            statement.setLong(1, key);
            ResultSet resultSet = statement.executeQuery();
            gummie = gummieExtractor.extractData(resultSet).stream().findAny();
            log.log(Level.INFO, "Found needed gummies by id in the database");
        } catch (SQLException ex) {
            log.log(Level.ERROR, "Error in get gummies function", ex);
            throw new DaoException("Error in process of getting gummies");
        } finally {
            lock.readLock().unlock();
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
        return gummie;
    }
    //
    /**
     * Find all gummies from database
     *
     * @param offset    - offset of found gummies
     * @param limit     - limit of found gummies
     * @param sortField - field to sort (model, brand, price, display size)
     * @param sortOrder - sort order (asc or desc)
     * @param query     - query for find
     * @return list of gummies
     * @throws DaoException throws when there is some errors during dao method execution
     */
    @Override
    public List<Gummie> findAll(int offset, int limit, SortField sortField, SortOrder sortOrder, String query) throws DaoException {
        List<Gummie> gummies = new ArrayList<>();
        String newQuery = "SELECT * FROM gummie";
        PreparedStatement statement = null;
        Connection connection = null;
        try {
            lock.readLock().lock();
            connection = connectionPool.getConnection();
            statement = connection.prepareStatement(newQuery);
            //statement.setInt(1, limit);
            //statement.setInt(2, offset);
            ResultSet result = statement.executeQuery();
            System.out.println("!");
            while (result.next()) {
                gummies.add(new Gummie(result.getLong("id"), result.getString("name")));
            }
            log.log(Level.INFO, "Found all gummies in the database");
        } catch (Exception e){
            System.out.println("exc");
        } finally {
            lock.readLock().unlock();
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ex) {
                    log.log(Level.ERROR, "Error in closing statement", ex);
                }
            }
            if (connection != null) {
                connectionPool.releaseConnection(connection);
            }
        }
        return gummies;
    }

    /**
     * Make sql query with sorting and finding
     *
     * @param sortField - field to sort
     * @param sortOrder - order to sort
     * @param query     - query to find
     * @return sql query
     */
    private String makeFindAllSQL(SortField sortField, SortOrder sortOrder, String query) {
        if (sortField != null || query != null && !query.equals("")) {
            StringBuilder sql = new StringBuilder(FIND_WITHOUT_OFFSET_AND_LIMIT);

            if (query != null && !query.equals("")) {
                sql.append("AND (" + "LOWER(PHONES.BRAND) LIKE LOWER('").append(query).append("%') ").
                        append("OR LOWER(PHONES.BRAND) LIKE LOWER('% ").append(query).append("%') ").
                        append("OR LOWER(PHONES.MODEL) LIKE LOWER('").append(query).append("%') ").
                        append("OR LOWER(PHONES.MODEL) LIKE LOWER('% ").append(query).append("%')").append(") ");
            }
            sql.append("AND PHONES.PRICE > 0 ");
            if (sortField != null) {
                sql.append("ORDER BY ").append(sortField.name()).append(" ");
                if (sortOrder != null) {
                    sql.append(sortOrder.name()).append(" ");
                } else {
                    sql.append("ASC ");
                }
            }
            sql.append("offset ? limit ?) ph");
            return sql.toString();
        } else {
            return SIMPLE_FIND_ALL_QUERY;
        }
    }
}
