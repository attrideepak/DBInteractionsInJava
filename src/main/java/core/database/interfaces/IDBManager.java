package core.database.interfaces;

import core.database.constants.DBType;
import org.bson.conversions.Bson;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface IDBManager {
    List<Map<String, String>> doSelect(
            DBType driver,
            String dbHost,
            int port,
            String dbName,
            boolean isSslEnabled,
            String userName,
            String password,
            String query)
            throws SQLException;

    int executeUpdate(
            DBType driver,
            String dbHost,
            int port,
            String dbName,
            boolean isSslEnabled,
            String userName,
            String password,
            String query)
            throws SQLException;

    List<String> doFind( //for mongo db
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
            throws SQLException, Exception;
}
