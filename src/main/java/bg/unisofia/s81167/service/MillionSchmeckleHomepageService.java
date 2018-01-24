package bg.unisofia.s81167.service;

import bg.unisofia.s81167.controller.*;
import bg.unisofia.s81167.encryption.*;
import bg.unisofia.s81167.model.PixelAllocationRequest;
import bg.unisofia.s81167.model.User;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.jaxrs.impl.HttpServletRequestFilter;

import javax.print.attribute.standard.Media;
import javax.servlet.ServletContext;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Path("")
public class MillionSchmeckleHomepageService {

    private ApplicationController controller;
    private TokenGenerator tokenGenerator = new SecureRandomTokenGenerator();

    public MillionSchmeckleHomepageService(ApplicationController controller) {
        this.controller = controller;
    }

    @POST
    @Path("login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response loginUser(User user, @Context UriInfo uriInfo) throws InvalidUserCredentials {
        controller.authenticateUser(user);
        final String token = getUserToken(user.getUsername());
        return Response.ok(token)
                .build();
    }

    @POST
    @Path("user")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response registerUser(User user, @Context UriInfo uriInfo) throws UserAlreadyExistsException {
        controller.registerUser(user);
        return Response.ok()
                .build();
    }

    @POST
    @Path("pixels")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response allocatePixels(PixelAllocationRequest request) throws PixelAllocationException {
        controller.allocatePixels(request.getUsername(), request.getPixels());
        return Response.ok()
                .header("Access-Control-Allow-Origin", "*")
                .build();
    }

    @GET
    @Path("pixels")
    @Produces("image/png")
    public Response getPixels() {
        return Response.ok()
                .entity(controller.getImageData())
                .build();
    }

    private String getUserToken(String username) {
        if (controller.userHasToken(username)) {
            return controller.getToken(username);
        } else{
            final String token = tokenGenerator.generateToken();
            controller.persistUserToken(username, token);
            return token;
        }
    }

}
