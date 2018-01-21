package bg.unisofia.s81167.util;

import bg.unisofia.s81167.persistence.DataSourceConfiguration;
import com.googlecode.catchexception.CatchException;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class GsonBasedFileConfigurationReaderTest {

    private FileConfigurationReader reader;
    private Path configurationPath;

    @Before
    public void setUp() throws Exception {
        this.configurationPath = getFilePath();
        this.reader = new GsonBasedFileConfigurationReader();
    }

    @Test
    public void testReadConfiguration_Successful() throws Exception {
        final DataSourceConfiguration configuration = reader.readConfiguration(DataSourceConfiguration.class,
                configurationPath);
        assertThat(configuration.getDriverClassName(), is("com.mysql.cj.jdbc.Driver"));
        assertThat(configuration.getUsername(), is("user1"));
        assertThat(configuration.getPassword(), is("password1"));
        assertThat(configuration.getDatabaseUrl(), is("data.base.url"));
        assertThat(configuration.getMaxActiveConnections(), is(15));
        assertThat(configuration.getMaxIdleConnections(), is(5));
        assertThat(configuration.getInitialPoolSize(), is(5));
        assertThat(configuration.getValidationQuery(), is("Select 1"));
    }

    @Test
    public void testReadConfiguration_Failure_NoSuchFile() {
        catchException(() -> reader.readConfiguration(DataSourceConfiguration.class, Paths.get("invalid.file")));
        assertThat(caughtException(), instanceOf(ConfigurationReadException.class));
    }

    private Path getFilePath() throws Exception {
        final ClassLoader classLoader = getClass().getClassLoader();
        final String uri = classLoader.getResource("configuration.json").getFile();

        return Paths.get(uri);
    }

}
