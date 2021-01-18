package utils.db_utils;

import core.ConfigInitializer;
import core.database.constants.DBType;
import core.database.db_manager.DBConfig;
import core.database.db_manager.DBManager;
import org.bson.conversions.Bson;
import org.testng.log4testng.Logger;

import java.io.File;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DBUtils {

    private static Logger logger = Logger.getLogger(DBUtils.class);
    private static final DBManager dbManager = new DBManager();

    private static final Properties dbProperties =
            ConfigInitializer.initializePropertiesFiles(
                    new File(System.getProperty("user.dir")).getAbsolutePath()
                            + "/configurations/database/"
                            + ConfigInitializer.activeEnv
                            + ".properties");

    private static final Properties dbConnectionProperties =
            ConfigInitializer.initializePropertiesFiles(
                    new File(System.getProperty("user.dir")).getAbsolutePath()
                            + "/configurations/database/configuration.properties");

    //String DB = zoomcar,inventory,pricing,payment
    public static DBConfig getDBConnectionConfig(String DB) throws NoSuchFieldException {
    String db = null;
    String host;
    DBConfig dbConfig = new DBConfig();
    for (Object entry : dbProperties.keySet()) {
        String dbKey = entry.toString();
        if (dbKey.toLowerCase().endsWith(".db." + DB.toLowerCase())) {
            db = dbKey;
            break;
        }
    }
        if (db != null) {
            host = db.split(".db." + DB.toLowerCase())[0];
        }else
            throw new NoSuchFieldException(
                        "provided '"
                                + DB
                                + "' DB not found in database/"
                                + ConfigInitializer.activeEnv
                                + ".properties");
        if ((host == null)
                || host.isEmpty()
                || !dbConnectionProperties.containsKey(host + ".type")
                || !dbConnectionProperties.containsKey(host + ".host")
                || !dbConnectionProperties.containsKey(host + ".port")
                || !dbConnectionProperties.containsKey(host + ".username")
                || !dbConnectionProperties.containsKey(host + ".password")) {
            throw new NoSuchFieldException(
                    "required '"
                            + host
                            + "' configurations are missing in database/config/configuration.properties");
        }else{
            dbConfig.setDbType(
                    DBType.valueOf(dbConnectionProperties.getProperty(host + ".type").toUpperCase()));
            dbConfig.setDbHost(dbConnectionProperties.getProperty(host + ".host"));
            dbConfig.setPort(Integer.parseInt(dbConnectionProperties.getProperty(host + ".port")));
            dbConfig.setDbName(dbProperties.getProperty(db));
            dbConfig.setUserName(dbConnectionProperties.getProperty(host + ".username"));
            dbConfig.setPassword(dbConnectionProperties.getProperty(host + ".password"));
        }

        return dbConfig;
    }

    public static List<Map<String,String>>doSelect(String DB, String query){
        DBConfig dbConfig = null;
        try {
            dbConfig = getDBConnectionConfig(DB);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        logger.debug(" dbConfig" + dbConfig);
        logger.info("DB: " + DB + " --> " + query);
        List<Map<String, String>> results = null;
        try {
            results = dbManager.doSelect(dbConfig, query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        logger.info(results.size() == 0 ? "no result found" : "No. of result rows : " + results.size());
        for (int i = 0; i < results.size(); i++) {
            logger.info("Query Result " + i + " : " + results.get(i).toString());
        }
        return results;
    }

    /**
     * @param DB params as inventory or Zoomcar
     * @param query
     * @return no of rows updated
     * @throws Exception
     */
    public static int executeUpdate(String DB, String query) throws Exception {
        DBConfig dbConfig = getDBConnectionConfig(DB);
        logger.debug(" dbConfig" + dbConfig);
        logger.info("DB: " + DB + " --> " + query);
        int noOfRows = dbManager.executeUpdate(dbConfig, query);
        return noOfRows;
    }

    public static List<String> doMongoFind(
            String DB, String table, Bson projectField, Bson filter, Bson sort) throws Exception {
        DBConfig dbConfig = getDBConnectionConfig(DB);
        logger.info("DB: " + DB + ", table: " + table + ", filter:" + filter);
        List<String> results = dbManager.doFind(dbConfig, table, projectField, filter, sort);
        logger.info(results.size() == 0 ? "no result found" : "No. of result rows : " + results.size());
        for (int i = 0; i < results.size(); i++) {
            logger.info("Query Result " + i + " : " + results.get(i));
        }
        return results;
    }

    public static void closeDbConnections() {
        try {
            dbManager.closeConnections();
        } catch (SQLException e) {
            logger.error("Unexpected Exception while db connection close ", e);
        }
    }
}

