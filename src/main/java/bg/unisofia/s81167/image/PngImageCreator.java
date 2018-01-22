package bg.unisofia.s81167.image;

import bg.unisofia.s81167.model.Pixel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;

public class PngImageCreator implements ImageCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PngImageCreator.class);

    private static final int SIZE_X = 1000;
    private static final int SIZE_Y = 1000;
    private static final String PNG_IMAGE_TYPE = "png";

    @Override
    public byte[] createImage(Collection<Pixel> pixels) throws ImageCreationException {
        final BufferedImage image = new BufferedImage(SIZE_X, SIZE_Y, BufferedImage.TYPE_INT_RGB);
        pixels.forEach(pixel -> setPixelInImage(pixel, image));

        return getImageBytes(image);
    }

    private void setPixelInImage(Pixel pixel, BufferedImage image) {
        final int pixelColor = new Color(pixel.getRed(), pixel.getGreen(), pixel.getBlue()).getRGB();
        image.setRGB(pixel.getX(), pixel.getY(), pixelColor);
    }

    private byte[] getImageBytes(BufferedImage image) throws ImageCreationException {
        try(final ByteArrayOutputStream baos = new ByteArrayOutputStream()){
            ImageIO.write(image, PNG_IMAGE_TYPE, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            final String message = "Failed to create image.";
            LOGGER.error(message, e);

            throw new ImageCreationException(message, e);
        }
    }

}
