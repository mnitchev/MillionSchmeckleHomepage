package bg.unisofia.s81167.service.reader;

import bg.unisofia.s81167.encryption.EncryptionException;
import bg.unisofia.s81167.encryption.Encryptor;
import bg.unisofia.s81167.encryption.Sha256Encryptor;
import bg.unisofia.s81167.model.User;
import com.google.gson.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class PasswordHashingJsonDeserializer implements JsonDeserializer<User>{

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordHashingJsonDeserializer.class);

    private final Encryptor encryptor = new Sha256Encryptor();

    @Override
    public User deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final User user = new Gson().fromJson(jsonElement, User.class);

        return getUserWithHashedPassword(user);
    }

    private User getUserWithHashedPassword(User user) {
        try {
            final String encryptedPassword = encryptor.encrypt(user.getPassword());
            user.setPassword(encryptedPassword);
            return user;
        } catch (EncryptionException e) {
            final String message = String.format("Failed to hash password for user : %s", user.getUsername());
            LOGGER.error(message, e);
            throw new JsonParseException(message);
        }
    }

}
