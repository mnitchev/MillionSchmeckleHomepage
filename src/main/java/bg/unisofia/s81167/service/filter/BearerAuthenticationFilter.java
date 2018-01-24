package bg.unisofia.s81167.service.filter;

import bg.unisofia.s81167.persistence.PersistenceException;
import bg.unisofia.s81167.persistence.user.MySqlUserDataAccessObject;
import bg.unisofia.s81167.persistence.user.UserDataAccessObject;
import org.apache.http.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class BearerAuthenticationFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(BearerAuthenticationFilter.class);

    private final UserDataAccessObject userDao;

    public BearerAuthenticationFilter() {
        this.userDao = new MySqlUserDataAccessObject();
    }

    @Override
    public void filter(ContainerRequestContext context) throws IOException {
        if (isUsersEndpoint(context)) {
            final String authHeader = context.getHeaderString(HttpHeaders.AUTHORIZATION);
            if(authHeader != null){
                final BearerAuthHeader header = decodeHeader(authHeader);
                validateUserCredentials(header);
            }
        }
    }

    private boolean isUsersEndpoint(ContainerRequestContext context) {
        return context.getUriInfo().getPath().endsWith("pixels") &&
                context.getMethod().equals("POST");
    }

    private void validateUserCredentials(BearerAuthHeader header) {
        try {
            if (!userDao.tokenValid(header.getToken())) {
                LOGGER.debug("Token : {} is not valid.", header.getToken());
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        } catch (PersistenceException e) {
            LOGGER.error("Failed to validate user credentials.", e);
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private BearerAuthHeader decodeHeader(String authHeader) {
        try {
            return new BearerAuthHeader(authHeader);
        } catch (InvalidAuthenticationTypeException e) {
            LOGGER.debug("Authentication method is not type Basic.");
            throw new WebApplicationException(Response.Status.BAD_REQUEST);
        }
    }

}
