package bg.unisofia.s81167.persistence.pixel;

import bg.unisofia.s81167.model.Pixel;
import bg.unisofia.s81167.model.User;
import bg.unisofia.s81167.persistence.DataSourceFactory;
import bg.unisofia.s81167.persistence.PersistenceException;
import bg.unisofia.s81167.persistence.PersistenceInitializationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

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
