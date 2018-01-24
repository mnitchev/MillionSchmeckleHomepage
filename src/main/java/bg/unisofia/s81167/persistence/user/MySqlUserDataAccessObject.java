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
    public static final String TOKEN_TABLE_NAME = "Tokens";

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

    @Override
    public boolean userHasToken(String username) throws PersistenceException {
        try(Connection connection = dataSource.getConnection()){
            return userHasToken(connection, username);
        }  catch (SQLException e){
            final String message = String.format("Failed to verify if user : %s has token.", username);
            LOGGER.error(message, e);
            throw new PersistenceException(message, e);
        }
    }

    @Override
    public void addUserToken(String username, String token) throws PersistenceException {
        try(Connection connection = dataSource.getConnection()){
            addUserToken(connection, username, token);
        } catch (SQLException e){
            final String message = String.format("Failed to verify if user : %s has token.", username);
            LOGGER.error(message, e);
            throw new PersistenceException(message, e);
        }
    }

    private void addUserToken(Connection connection, String username, String token) throws SQLException {
        final String addToken = UsersPreparedStatements.INSERT_TOKEN.getStatement(TOKEN_TABLE_NAME);
        final PreparedStatement statement = connection.prepareStatement(addToken);
        statement.setString(1, username);
        statement.setString(2, token);

        statement.executeUpdate();
    }

    @Override
    public String getUserToken(String username) throws PersistenceException {
        try (Connection connection = dataSource.getConnection()) {
            return getUserToken(connection, username);
        }  catch (SQLException e){
            final String message = String.format("Failed to retrieve token for user : %s.", username);
            LOGGER.error(message, e);
            throw new PersistenceException(message, e);
        }
    }

    @Override
    public boolean tokenValid(String token) throws PersistenceException {
        try (Connection connection = dataSource.getConnection()) {
            return tokenValid(connection, token);
        } catch (SQLException e) {
            final String message = String.format("Failed to validate token : %s.", token);
            LOGGER.error(message, e);
            throw new PersistenceException(message, e);
        }
    }

    private boolean tokenValid(Connection connection, String token) throws SQLException {
        final String getTokenQuery = UsersPreparedStatements.GET_TOKEN.getStatement(TOKEN_TABLE_NAME);
        final PreparedStatement statement = connection.prepareStatement(getTokenQuery);
        statement.setString(1, token);

        final ResultSet resultSet = statement.executeQuery();
        return resultSet.next();
    }

    private String getUserToken(Connection connection, String username) throws SQLException {
        final ResultSet resultSet = executeGetUserTokenQuery(connection, username);
        resultSet.next();
        return resultSet.getString("token");
    }

    private boolean userHasToken(Connection connection, String username) throws SQLException {
        final ResultSet resultSet = executeGetUserTokenQuery(connection, username);

        return resultSet.next();
    }

    private ResultSet executeGetUserTokenQuery(Connection connection, String username) throws SQLException {
        final String getUserTokenStatement = UsersPreparedStatements.GET_USER_TOKEN.getStatement(TOKEN_TABLE_NAME);
        final PreparedStatement statement = connection.prepareStatement(getUserTokenStatement);
        statement.setString(1, username);
        return statement.executeQuery();
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
