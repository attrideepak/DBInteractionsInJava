package core.database.constants;

public enum DBType {
    MYSQL("com.mysql.cj.jdbc.Driver"),
    POSTGRES("org.postgresql.Driver"),
    MONGO("noDriverClassRequired"),
    ;

    String driver;


    DBType(String driver) {
        this.driver = driver;
    }

    public String getDriver() {
        return driver;
    }
}
