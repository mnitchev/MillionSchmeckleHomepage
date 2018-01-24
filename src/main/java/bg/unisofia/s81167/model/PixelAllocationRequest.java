package bg.unisofia.s81167.model;

import java.util.Collection;

public class PixelAllocationRequest {

    private String username;
    private Collection<Pixel> pixels;

    public PixelAllocationRequest(String username, Collection<Pixel> pixels) {
        this.username = username;
        this.pixels = pixels;
    }

    public String getUsername() {
        return username;
    }

    public Collection<Pixel> getPixels() {
        return pixels;
    }

}
