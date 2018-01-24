package bg.unisofia.s81167.service.reader;

import bg.unisofia.s81167.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.MessageBodyReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

public class JsonBodyReader<T> implements MessageBodyReader<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonBodyReader.class);

    private final Gson gson;

    public JsonBodyReader() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new PasswordHashingJsonDeserializer())
                .create();
    }

    @Override
    public boolean isReadable(Class<?> aClass, Type type, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public T readFrom(Class<T> aClass, Type type, Annotation[] annotations, MediaType mediaType,
                      MultivaluedMap<String, String> multivaluedMap, InputStream inputStream) throws IOException, WebApplicationException {
        try(InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)){
            return gson.fromJson(reader, type);
        } catch(IOException | JsonSyntaxException e){
            LOGGER.error("Failed to deserialize entity.", e);
            throw new WebApplicationException(Response.status(Response.Status.BAD_REQUEST).build());
        }
    }

}
