package bg.unisofia.s81167.persistence.pixel;

import bg.unisofia.s81167.model.Pixel;
import bg.unisofia.s81167.model.User;
import bg.unisofia.s81167.persistence.PersistenceException;

import java.util.Collection;

public interface PixelDataAccessObject {

    boolean allocatePixel(User user, Pixel pixelSpace) throws PersistenceException;

    Collection<Pixel> getAllPixels() throws PersistenceException;

}
