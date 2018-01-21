package bg.unisofia.s81167.persistence;

import bg.unisofia.s81167.model.Pixel;
import bg.unisofia.s81167.model.User;

public interface PixelDataAccessObject {

    boolean allocatePixel(User user, Pixel pixelSpace) throws PersistenceException;

}
