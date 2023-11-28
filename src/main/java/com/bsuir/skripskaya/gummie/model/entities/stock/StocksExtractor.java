package com.bsuir.skripskaya.gummie.model.entities.stock;

import com.bsuir.skripskaya.gummie.model.dao.GummieDao;
import com.bsuir.skripskaya.gummie.model.dao.impl.JdbcGummieDao;
import com.bsuir.skripskaya.gummie.model.exceptions.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StocksExtractor {
    private GummieDao gummieDao = JdbcGummieDao.getInstance();

    public List<Stock> extractData(ResultSet resultSet) throws SQLException, DaoException {
        List<Stock> stocks = new ArrayList<>();
        while (resultSet.next()) {
            Stock stock = new Stock();
            stock.setGummie(gummieDao.get(resultSet.getLong("GUMMIEID")).orElse(null));
            stock.setStock(resultSet.getInt("STOCK"));
            stock.setReserved(resultSet.getInt("RESERVED"));
            stocks.add(stock);
        }
        return stocks;
    }
}
