package bg.unisofia.s81167.image;

import bg.unisofia.s81167.model.Pixel;

import java.util.Collection;

public interface ImageCreator {

    byte[] createImage(Collection<Pixel> pixels) throws ImageCreationException;

}
