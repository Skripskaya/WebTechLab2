package com.bsuir.skripskaya.gummie.model.entities.gummie;

import com.bsuir.skripskaya.gummie.model.exceptions.DaoException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GummieExtractor {

    public List<Gummie> extractData(ResultSet resultSet) throws SQLException, DaoException {
        List<Gummie> gummies = new ArrayList<>();
        while (resultSet.next()) {
            Gummie gummie = new Gummie();
            gummie.setId(resultSet.getLong("ID"));
            gummie.setName(resultSet.getString("NAME"));
            gummie.setPrice(resultSet.getBigDecimal("PRICE"));
            gummies.add(gummie);
        }

        return gummies;
    }
}
