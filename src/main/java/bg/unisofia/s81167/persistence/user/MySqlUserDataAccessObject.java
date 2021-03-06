package bg.unisofia.s81167.persistence.user;

import bg.unisofia.s81167.model.User;
import bg.unisofia.s81167.persistence.DataSourceFactory;
import bg.unisofia.s81167.persistence.PersistenceException;
import bg.unisofia.s81167.persistence.pixel.MySqlPixelDataAccessObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySqlUserDataAccessObject implements UserDataAccessObject {

    public static final String TABLE_NAME = "Users";
    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlPixelDataAccessObject.class);

    private final DataSource dataSource;

    public MySqlUserDataAccessObject() {
        this.dataSource = DataSourceFactory.getDataSourceSingleton();
    }

    @Override
    public boolean userExists(User user) throws PersistenceException {
        try (Connection connection = dataSource.getConnection()) {
            return userExists(connection, user);
        } catch (SQLException e){
            final String message = String.format("Failed to verify if user : %s exists.", user.getUsername());
            LOGGER.error(message, e);
            throw new PersistenceException(message, e);
        }
    }

    @Override
    public boolean userHasValidCredentials(User user) throws PersistenceException {
        try (Connection connection = dataSource.getConnection()) {
            if (!userExists(user)) {
                LOGGER.debug("User {} does not exists.", user.getUsername());
                return false;
            }
            final User actualUser = getUser(connection, user);
            return user.equals(actualUser);
        } catch (SQLException e){
            final String message = String.format("Failed to verify user credentials for user : %s.",
                    user.getUsername());
            LOGGER.error(message, e);
            throw new PersistenceException(message, e);
        }
    }

    @Override
    public void registerUser(User user) throws PersistenceException {
        try (Connection connection = dataSource.getConnection()) {
            registerUser(connection, user);
        } catch (SQLException e){
            final String message = String.format("Failed to register user : %s.", user.getUsername());
            LOGGER.error(message, e);
            throw new PersistenceException(message, e);
        }
    }

    private User getUser(Connection connection, User user) throws SQLException {
        final ResultSet resultSet = executeGetUserQuery(connection, user);
        resultSet.next();
        final String username = resultSet.getString("username");
        final String password = resultSet.getString("password");

        return new User(username, password);
    }

    private void registerUser(Connection connection, User user) throws SQLException {
        final String insertUserPreparedStatement = UsersPreparedStatements.INSERT_USER.getStatement(TABLE_NAME);
        final PreparedStatement statement = connection.prepareStatement(insertUserPreparedStatement);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getPassword());

        statement.executeUpdate();
    }

    private boolean userExists(Connection connection, User user) throws SQLException {
        final ResultSet resultSet = executeGetUserQuery(connection, user);

        return resultSet.next();
    }

    private ResultSet executeGetUserQuery(Connection connection, User user) throws SQLException {
        final String getUserPreparedStatement = UsersPreparedStatements.GET_USER.getStatement(TABLE_NAME);
        final PreparedStatement statement = connection.prepareStatement(getUserPreparedStatement);
        statement.setString(1, user.getUsername());
        return statement.executeQuery();
    }

}
