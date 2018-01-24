package bg.unisofia.s81167.encryption;

import java.util.UUID;

public class SecureRandomTokenGenerator implements TokenGenerator {

    @Override
    public String generateToken() {
        return UUID.randomUUID().toString();
    }

}
