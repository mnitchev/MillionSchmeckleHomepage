package bg.unisofia.s81167.controller;


import bg.unisofia.s81167.model.Pixel;
import bg.unisofia.s81167.model.User;

import java.util.Collection;

public interface ApplicationController {

    void allocatePixels(String username, Collection<Pixel> space) throws PixelAllocationException;

    byte[] getImageData();

    void registerUser(User user) throws UserAlreadyExistsException;

    void authenticateUser(User user) throws InvalidUserCredentials;

    boolean userHasToken(String username);

    String getToken(String username);

    void persistUserToken(String username, String token);
}
