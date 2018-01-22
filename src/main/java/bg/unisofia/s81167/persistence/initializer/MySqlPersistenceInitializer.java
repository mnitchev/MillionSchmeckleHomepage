package bg.unisofia.s81167.persistence.initializer;

import bg.unisofia.s81167.persistence.DataSourceFactory;
import bg.unisofia.s81167.persistence.PersistenceInitializationException;
import bg.unisofia.s81167.persistence.pixel.MySqlPixelDataAccessObject;
import bg.unisofia.s81167.persistence.pixel.PixelsPreparedStatements;
import bg.unisofia.s81167.persistence.user.MySqlUserDataAccessObject;
import bg.unisofia.s81167.persistence.user.UsersPreparedStatements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

public class MySqlPersistenceInitializer implements PersistenceInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlPixelDataAccessObject.class);

    private static final String USERS_TABLE_NAME = MySqlUserDataAccessObject.TABLE_NAME;
    private static final String PIXELS_TABLE_NAME = MySqlPixelDataAccessObject.TABLE_NAME;

    private DataSource dataSource;

    public MySqlPersistenceInitializer() {
        this.dataSource = DataSourceFactory.getDataSourceSingleton();
    }

    @Override
    public void initialize() {
        initializeUsersTable();
        initializePixelsTable();
    }

    private void initializeUsersTable() {
        final String createTableStatement = UsersPreparedStatements.CREATE_TABLE.getStatement(USERS_TABLE_NAME);
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metadata = connection.getMetaData();
            if (tableExists(metadata, USERS_TABLE_NAME)) {
                LOGGER.debug("Table {} already exists.", USERS_TABLE_NAME);
                return;
            }
            LOGGER.debug("Table {} does not exist. Creating table.", USERS_TABLE_NAME);
            createTable(connection, createTableStatement);
        } catch (SQLException e) {
            final String message = "Failed to initialize database.";
            LOGGER.error(message, e);

            throw new PersistenceInitializationException(message, e);
        }
    }

    private void initializePixelsTable() {
        final String createTableStatement = PixelsPreparedStatements.CREATE_TABLE.getStatement(PIXELS_TABLE_NAME);
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metadata = connection.getMetaData();
            if (tableExists(metadata, PIXELS_TABLE_NAME)) {
                LOGGER.debug("Table {} already exists.", PIXELS_TABLE_NAME);
                return;
            }
            LOGGER.debug("Table {} does not exist. Creating table.", PIXELS_TABLE_NAME);
            createTable(connection, createTableStatement);
            addDeleteOldPixelsEvent(connection);
        } catch (SQLException e) {
            final String message = "Failed to initialize database.";
            LOGGER.error(message, e);

            throw new PersistenceInitializationException(message, e);
        }
    }

    private void createTable(Connection connection, String createTablePreparedStatement) throws SQLException {
        final PreparedStatement statement = connection.prepareStatement(createTablePreparedStatement);
        statement.executeUpdate();
    }

    private boolean tableExists(DatabaseMetaData metadata, String tableName) throws SQLException {
        try (ResultSet tables = metadata.getTables(null, null, tableName, null)) {
            return tables.next();
        }
    }

    private void addDeleteOldPixelsEvent(Connection connection) throws SQLException {
        final String updateEvent = PixelsPreparedStatements.DELETE_OLD_EVENT.getStatement(PIXELS_TABLE_NAME);
        final PreparedStatement statement = connection.prepareStatement(updateEvent);

        statement.execute();
    }

}
