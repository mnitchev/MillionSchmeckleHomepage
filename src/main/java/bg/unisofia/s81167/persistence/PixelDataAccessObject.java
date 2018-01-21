package bg.unisofia.s81167.persistence;

import bg.unisofia.s81167.model.Pixel;
import bg.unisofia.s81167.model.User;
import bg.unisofia.s81167.service.PixelAllocationException;

public interface PixelDataAccessObject {

    boolean allocatePixel(User user, Pixel pixelSpace) throws PixelAllocationException, PersistenceException;

}
