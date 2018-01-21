package bg.unisofia.s81167.persistence;

import bg.unisofia.s81167.util.FileConfigurationReader;
import bg.unisofia.s81167.util.GsonBasedFileConfigurationReader;
import org.apache.commons.dbcp.BasicDataSource;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DataSourceFactory {

    private static final String DEFAULT_CONFIGURATION_FILE_PATH = "configuration.json";
    private static DataSource pixelDao;

    public static DataSource getDataSourceSingleton() {
        if (pixelDao == null){
            final DataSourceConfiguration configuration = readDataSourceConfiguration();
            pixelDao = createDatabaseObject(configuration);
            return pixelDao;
        }
        return pixelDao;
    }

    private static DataSourceConfiguration readDataSourceConfiguration() {
        final FileConfigurationReader reader = new GsonBasedFileConfigurationReader();
        final Path filePath = Paths.get(DEFAULT_CONFIGURATION_FILE_PATH);

        return reader.readConfiguration(DataSourceConfiguration.class, filePath);
    }

    private static DataSource createDatabaseObject(DataSourceConfiguration configuration) {
        final BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(configuration.getDriverClassName());
        dataSource.setUsername(configuration.getUsername());
        dataSource.setPassword(configuration.getPassword());
        dataSource.setUrl(configuration.getDatabaseUrl());
        dataSource.setMaxActive(configuration.getMaxActiveConnections());
        dataSource.setMaxIdle(configuration.getMaxIdleConnections());
        dataSource.setInitialSize(configuration.getInitialPoolSize());
        dataSource.setValidationQuery(configuration.getValidationQuery());

        return dataSource;
    }

}
