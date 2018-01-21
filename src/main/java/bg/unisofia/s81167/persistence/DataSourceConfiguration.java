package bg.unisofia.s81167.persistence;

public class DataSourceConfiguration {

    private String driverClassName;
    private String username;
    private String password;
    private String databaseUrl;
    private int maxActiveConnections;
    private int maxIdleConnections;
    private int initialPoolSize;
    private String validationQuery;

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public int getMaxActiveConnections() {
        return maxActiveConnections;
    }

    public int getMaxIdleConnections() {
        return maxIdleConnections;
    }

    public int getInitialPoolSize() {
        return initialPoolSize;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

}
