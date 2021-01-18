package core.database.connection_manager;

import com.mongodb.MongoClient;
import core.database.constants.DBType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class ConnectionFactory {

    private static HashMap<String, Connection> knownDBConnections = new HashMap<>();
    protected static HashMap<String, MongoClient> knownMongoConnections = new HashMap<>();
    private Connection connection;
    private MongoClient mongoClient;

    public Connection getConnection(
            DBType driver,
            String dbHost,
            int port,
            String dbName,
            boolean isSslEnabled,
            String userName,
            String password){
        String url;
        boolean createNew = false;
        switch (driver){
            case MYSQL:
                url = "jdbc:mysql://" + dbHost + ":" + port + "/" + dbName + "?useSSL=" + isSslEnabled;
                if (knownDBConnections.containsKey(url)){
                    connection = knownDBConnections.get(url);
                    try {
                        if (!connection.isValid(30)) {
                            createNew = true;
                            connection.close();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    createNew = true;
                }
                if (createNew) {
                    connection = new MySqlConnectionManager().getConnection(url, userName, password);
                    knownDBConnections.put(url, connection);
                }
                break;
            case POSTGRES:
                url = "jdbc:postgresql://" + dbHost + ":" + port + "/" + dbName + "?useSSL=" + isSslEnabled;
                if (knownDBConnections.containsKey(url)) {
                    connection = knownDBConnections.get(url);
                    try {
                        if (!connection.isValid(30)) {
                            createNew = true;
                            connection.close();
                        }
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                } else {
                    createNew = true;
                }
                if (createNew) {
                    connection = new PostGresConnectionManager().getConnection(url, userName, password);
                    knownDBConnections.put(url, connection);
                }
                break;
            default:
                throw new RuntimeException("Unknown driver passed.");
        }

        return connection;
    }

    public MongoClient getMongoConnection(
            DBType driver,
            String dbHost,
            int port,
            String dbName,
            boolean isSslEnabled,
            String userName,
            String password) {
        String url;
        if (driver.equals(driver)) {
            url = "mongodb://" + userName + ":" + password + "@" + dbHost;
            if (knownMongoConnections.containsKey(url)) {
                mongoClient = knownMongoConnections.get(url);
            } else {
                mongoClient = new MongoConnectionManager().getClient(url, dbName);
                knownMongoConnections.putIfAbsent(url, mongoClient);
            }
        }
        return mongoClient;
    }

    public HashMap<String, Connection> finalizeConnections() {
        return knownDBConnections;
    }
}
