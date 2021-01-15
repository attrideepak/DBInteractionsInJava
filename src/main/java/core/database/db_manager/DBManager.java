package core.database.db_manager;

import core.database.constants.DBType;
import core.database.interfaces.IDBManager;
import org.bson.conversions.Bson;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;

public class DBManager implements IDBManager {

    @Override
    public List<LinkedHashMap<String, String>> doSelect(DBType driver, String dbHost, int port, String dbName, boolean isSslEnabled, String userName, String password, String query) throws SQLException {
        return null;
    }

    @Override
    public int executeUpdate(DBType driver, String dbHost, int port, String dbName, boolean isSslEnabled, String userName, String password, String query) throws SQLException {
        return 0;
    }

    @Override
    public List<String> doFind(DBType driver, String dbHost, int port, String dbName, boolean isSslEnabled, String userName, String password, String tableName, Bson projectField, Bson queryFilter, Bson sortField) throws SQLException, Exception {
        return null;
    }
}
