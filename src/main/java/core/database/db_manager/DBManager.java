package core.database.db_manager;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import core.database.connection_manager.ConnectionFactory;
import core.database.constants.DBType;
import core.database.interfaces.IDBManager;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.testng.log4testng.Logger;

import java.sql.*;
import java.util.*;

public class DBManager implements IDBManager {

    private static Logger logger = Logger.getLogger(DBManager.class);

    @Override
    public List<Map<String, String>> doSelect(DBType driver, String dbHost, int port, String dbName, boolean isSslEnabled, String userName, String password, String query) throws SQLException {
        List<Map<String, String>> results = new ArrayList<>();
        Connection connection =
                new ConnectionFactory()
                        .getConnection(driver, dbHost, port, dbName, isSslEnabled, userName, password);
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)){
            while (resultSet.next()){
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                Map<String, String> rowData = new LinkedHashMap<>();
                for (int col = 1; col <= resultSetMetaData.getColumnCount(); col++) {
                    rowData.put(resultSetMetaData.getColumnLabel(col), resultSet.getString(col));
                }
                results.add(rowData);
            }

        }
        return results;
    }

    public List<Map<String,String>> doSelect(DBConfig dbConfig, String query) throws SQLException {
            return doSelect(
                    dbConfig.getDbType(),
                    dbConfig.getDbHost(),
                    dbConfig.getPort(),
                    dbConfig.getDbName(),
                    dbConfig.isSslEnabled(),
                    dbConfig.getUserName(),
                    dbConfig.getPassword(),
                    query);
    }

    public int executeUpdate(
            DBType driver,
            String dbHost,
            int port,
            String dbName,
            boolean isSslEnabled,
            String userName,
            String password,
            String query)
            throws SQLException {

        Connection connection =
                new ConnectionFactory()
                        .getConnection(driver, dbHost, port, dbName, isSslEnabled, userName, password);
        int noOfRows;
        try (Statement st = connection.createStatement()) {
            noOfRows = st.executeUpdate(query);
        }
        System.out.println("No. of rows affected after running " + query + " is: " + noOfRows);
        return noOfRows;
    }

    public int executeUpdate(DBConfig dbConfig, String query) throws SQLException {

        return executeUpdate(
                dbConfig.getDbType(),
                dbConfig.getDbHost(),
                dbConfig.getPort(),
                dbConfig.getDbName(),
                dbConfig.isSslEnabled(),
                dbConfig.getUserName(),
                dbConfig.getPassword(),
                query);
    }

    @Override
    public List<String> doFind(
            DBType driver,
            String dbHost,
            int port,
            String dbName,
            boolean isSslEnabled,
            String userName,
            String password,
            String tableName,
            Bson projectField,
            Bson queryFilter,
            Bson sortField)
            throws Exception {
        List<String> resultList = new ArrayList<>();
        MongoClient mongoClient =
                new ConnectionFactory()
                        .getMongoConnection(driver, dbHost, port, dbName, isSslEnabled, userName, password);
        MongoDatabase database = mongoClient.getDatabase(dbName);
        MongoCollection<Document> collection = database.getCollection(tableName);
        FindIterable<Document> result = null;
        result = collection.find().filter(queryFilter).projection(projectField).sort(sortField);
        for (Document doc : result) {
            resultList.add(doc.toJson());
        }
        return resultList;
    }

    public List<String> doFind(
            DBConfig dbConfig, String tableName, Bson projectField, Bson queryFilter, Bson sortField)
            throws Exception {
        return doFind(
                dbConfig.getDbType(),
                dbConfig.getDbHost(),
                dbConfig.getPort(),
                dbConfig.getDbName(),
                dbConfig.isSslEnabled(),
                dbConfig.getUserName(),
                dbConfig.getPassword(),
                tableName,
                projectField,
                queryFilter,
                sortField);
    }

    public void closeConnections() throws SQLException {
        HashMap<String, Connection> knownDBConnections = new ConnectionFactory().finalizeConnections();
        for (String key : knownDBConnections.keySet()) {
            logger.info(key);
            knownDBConnections.get(key).close();
        }
        logger.info("DB connection(s) closed");
    }

}
