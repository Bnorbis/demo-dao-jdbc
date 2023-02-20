package model.dao;

import db.DB;
import model.dao.impl.SellerDaoJDBC;

import java.sql.SQLException;

public class DaoFactory {
    public static SellerDao createSellerDao (){
        try {
            return new SellerDaoJDBC(DB.getConnection());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
