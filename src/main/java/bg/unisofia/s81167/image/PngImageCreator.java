package bg.unisofia.s81167.image;

import bg.unisofia.s81167.model.Pixel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;

public class PngImageCreator implements ImageCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(PngImageCreator.class);

    private static final int SIZE_X = 100;
    private static final int SIZE_Y = 100;
    private static final String PNG_IMAGE_TYPE = "png";

    @Override
    public byte[] createImage(Collection<Pixel> pixels) throws ImageCreationException {
        final BufferedImage image = new BufferedImage(SIZE_X, SIZE_Y, BufferedImage.TYPE_INT_RGB);
        pixels.forEach(pixel -> setPixelInImage(pixel, image));

        savePNG(image, "/home/mario/workspace/tmp/image.png");
        return getImageBytes(image);
    }

    private static void savePNG( final BufferedImage bi, final String path ){
        try {
            RenderedImage rendImage = bi;
//            ImageIO.write(rendImage, "bmp", new File(path));
            ImageIO.write(rendImage, "PNG", new File(path));
            //ImageIO.write(rendImage, "jpeg", new File(path));
        } catch ( IOException e) {
            e.printStackTrace();
        }
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
