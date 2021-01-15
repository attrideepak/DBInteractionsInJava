package core.database.connection_manager;

import core.database.constants.DBType;
import core.database.interfaces.IConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlConnectionManager implements IConnectionManager {
    @Override
    public Connection getConnection(String url, String userName, String password) {
        Connection connection = null;

        try {
            Class.forName(DBType.MYSQL.getDriver());
            connection = DriverManager.getConnection(url, userName, password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}

