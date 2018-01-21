package bg.unisofia.s81167.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Sha256Encryptor implements Encryptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(Encryptor.class);
    private static final String ENCRPYTION_ALGORYTHM = "SHA-256";

    public String encrypt(String value  ) throws EncryptionException {
        try {
            return encryptValue(value);
        } catch (NoSuchAlgorithmException e) {
            final String message = String.format("Failed to encrypt value using algorithm : %s",
                    ENCRPYTION_ALGORYTHM);
            LOGGER.error(message, e);
            throw new EncryptionException(message, e);
        }
    }

    private String encryptValue(String value) throws NoSuchAlgorithmException {
        final MessageDigest md = MessageDigest.getInstance(ENCRPYTION_ALGORYTHM);
        final byte[] bytes = md.digest(value.getBytes());
        final StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

}
