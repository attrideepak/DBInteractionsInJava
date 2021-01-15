package core.database.connection_manager;

import core.database.constants.DBType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

public class ConnectionFactory {

    private static HashMap<String, Connection> knownDBConnections = new HashMap<>();
    private Connection connection;

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
            default:
                throw new RuntimeException("Unknown driver passed.");
        }

        return connection;
    }
}
