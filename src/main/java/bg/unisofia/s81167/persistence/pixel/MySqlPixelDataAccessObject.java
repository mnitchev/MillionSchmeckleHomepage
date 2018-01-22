package bg.unisofia.s81167.persistence.pixel;

import bg.unisofia.s81167.model.Pixel;
import bg.unisofia.s81167.model.User;
import bg.unisofia.s81167.persistence.DataSourceFactory;
import bg.unisofia.s81167.persistence.PersistenceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MySqlPixelDataAccessObject implements PixelDataAccessObject {

    public static final String TABLE_NAME = "Pixels";
    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlPixelDataAccessObject.class);
    private final DataSource dataSource;

    public MySqlPixelDataAccessObject() {
        this.dataSource = DataSourceFactory.getDataSourceSingleton();
    }

    @Override
    public boolean allocatePixel(User user, Pixel pixel) throws PersistenceException {
        try (Connection connection = dataSource.getConnection()) {
            if (pixelExists(connection, pixel)) {
                LOGGER.debug("Pixel : {} already allocated.", pixel);
                return false;
            }
            return persistPixelsInDatabase(connection, user, pixel);
        } catch (SQLException e) {
            final String message = String.format("Failed to allocate pixels %s, for user %s.", pixel,
                    user.getUsername());
            LOGGER.error(message, e);

            throw new PersistenceException(message, e);
        }
    }

    @Override
    public Collection<Pixel> getAllPixels() throws PersistenceException {
        try (Connection connection = dataSource.getConnection()) {
            return getAllPixelsFromDatabase(connection);
        } catch (SQLException e) {
            final String message = "Failed to get all pixles from database.";
            LOGGER.error(message, e);

            throw new PersistenceException(message, e);
        }
    }

    private Collection<Pixel> getAllPixelsFromDatabase(Connection connection) throws SQLException {
        final ResultSet resultSet = executeGetAllPixelsQuery(connection);

        return getPixelsFromResultSet(resultSet);
    }

    private Collection<Pixel> getPixelsFromResultSet(ResultSet resultSet) throws SQLException {
        final List<Pixel> pixels = new ArrayList<>();
        while(resultSet.next()){
            final Pixel pixel = extractPixel(resultSet);
            pixels.add(pixel);
        }
        return pixels;
    }

    private Pixel extractPixel(ResultSet resultSet) throws SQLException {
        final int x = resultSet.getInt("x");
        final int y = resultSet.getInt("y");
        final int red = resultSet.getInt("red");
        final int green = resultSet.getInt("green");
        final int blue = resultSet.getInt("blue");

        return new Pixel.Builder()
                .withPosition(x, y)
                .withColor(red, green, blue)
                .build();
    }

    private ResultSet executeGetAllPixelsQuery(Connection connection) throws SQLException {
        final String getAllPixelsStatement = PixelsPreparedStatements.GET_ALL_PIXELS.getStatement(TABLE_NAME);
        final PreparedStatement statement = connection.prepareStatement(getAllPixelsStatement);
        return statement.executeQuery();
    }

    private boolean persistPixelsInDatabase(Connection connection, User user, Pixel pixel) throws SQLException {
        LOGGER.debug("Allocating pixel : {}, for user : {}.", pixel, user.getUsername());

        final String insertPixelStatement = PixelsPreparedStatements.INSERT_PIXEL.getStatement(TABLE_NAME);
        final PreparedStatement statement = connection.prepareStatement(insertPixelStatement);
        statement.setString(1, user.getUsername());
        statement.setInt(2, pixel.getX());
        statement.setInt(3, pixel.getY());
        statement.setInt(4, pixel.getRed());
        statement.setInt(5, pixel.getGreen());
        statement.setInt(6, pixel.getBlue());

        final int rowsUpdated = statement.executeUpdate();
        return rowsUpdated == 1;
    }

    private boolean pixelExists(Connection connection, Pixel pixel) throws SQLException {
        final String getPixelPreparedStatement = PixelsPreparedStatements.GET_PIXEL.getStatement(TABLE_NAME);
        final PreparedStatement statement = connection.prepareStatement(getPixelPreparedStatement);
        statement.setInt(1, pixel.getX());
        statement.setInt(2, pixel.getY());

        final ResultSet resultSet = statement.executeQuery();

        return resultSet.next();
    }

}
