package bg.unisofia.s81167.util;

import com.google.gson.Gson;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class GsonBasedFileConfigurationReader implements FileConfigurationReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(GsonBasedFileConfigurationReader.class);
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    private Gson gson = new Gson();

    @Override
    public <T> T readConfiguration(Class<T> configurationClass, Path filePath) throws ConfigurationReadException {
        final String fileContents = readFile(filePath);

        return gson.fromJson(fileContents, configurationClass);
    }

    private String readFile(Path filePath) throws ConfigurationReadException {
        try{
            final byte[] fileBytes = Files.readAllBytes(filePath);
            return IOUtils.toString(fileBytes, DEFAULT_CHARSET.name());
        } catch (IOException e){
            final String message = String.format("Failed to read configuration file at : %s.", filePath);
            LOGGER.error(message, e);
            throw new ConfigurationReadException(message, e);
        }
    }

}
