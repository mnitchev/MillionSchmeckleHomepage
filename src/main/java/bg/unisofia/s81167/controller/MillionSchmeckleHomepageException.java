package bg.unisofia.s81167.controller;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class MillionSchmeckleHomepageException extends WebApplicationException {

    public MillionSchmeckleHomepageException(String message, Throwable cause) {
        super(message, cause, Response.Status.INTERNAL_SERVER_ERROR);
    }

    public MillionSchmeckleHomepageException(Throwable cause) {
        super(cause, Response.Status.INTERNAL_SERVER_ERROR);
    }

}
