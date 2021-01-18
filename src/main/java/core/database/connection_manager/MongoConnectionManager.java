package core.database.connection_manager;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import core.database.constants.DBType;
import core.database.interfaces.IConnectionManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MongoConnectionManager implements IConnectionManager {
    @Override
    public Connection getConnection(String url, String userName, String password) {
        Connection connection = null;

        try {
            Class.forName(DBType.MONGO.getDriver());
            connection = DriverManager.getConnection(url, userName, password);

        } catch (ClassNotFoundException
                | SQLException
               e) {
            e.printStackTrace();
        }
        return connection;
    }

    public MongoClient getClient(String url, String dbName) {
        MongoClientURI uri = new MongoClientURI(url + "/?authSource=" + dbName);
        return new com.mongodb.MongoClient(uri);
    }
}
