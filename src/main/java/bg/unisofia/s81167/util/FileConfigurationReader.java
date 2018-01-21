package bg.unisofia.s81167.util;

import java.nio.file.Path;

public interface FileConfigurationReader {

    <T> T readConfiguration(Class<T> configurationClass, Path filePath) throws ConfigurationReadException;
}
