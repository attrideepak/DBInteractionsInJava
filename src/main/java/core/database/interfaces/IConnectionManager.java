package core.database.interfaces;

import java.sql.Connection;

public interface IConnectionManager {
    Connection getConnection(String url, String userName, String password);
}
