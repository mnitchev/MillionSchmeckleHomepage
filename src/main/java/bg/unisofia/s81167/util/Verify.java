package bg.unisofia.s81167.util;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

public class Verify {

    public static void verifyNotNull(Object... values){
        for (Object value : values) {
            if (value == null){
                throw new WebApplicationException(Response.Status.BAD_REQUEST);
            }
        }
    }

}
