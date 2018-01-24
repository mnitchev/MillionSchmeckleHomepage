package bg.unisofia.s81167.service.filter;

public class BearerAuthHeader {

    private final String token;

    public BearerAuthHeader(String authHeader) throws InvalidAuthenticationTypeException {
        this.token = parseHeader(authHeader);
    }

    public String getToken() {
        return token;
    }

    private String parseHeader(String authHeader) throws InvalidAuthenticationTypeException{
        if (!authHeader.startsWith("Bearer ")) {
            throw new InvalidAuthenticationTypeException("Header not Bearer authentication type.");
        }
        return decodeHeader(authHeader);
    }

    private String decodeHeader(String authHeader){
        final String[] split = authHeader.split(" ");
        return split[1];
    }

}
