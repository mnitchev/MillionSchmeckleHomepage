package bg.unisofia.s81167.encryption;

public interface Encryptor {

    String encrypt(String value) throws EncryptionException;

}
