package bg.unisofia.s81167.model;

import java.util.Objects;

public class Pixel {

    private int x;
    private int y;
    private int red;
    private int green;
    private int blue;

    public static class Builder{

        private int x;
        private int y;
        private int red;
        private int green;
        private int blue;

        public Builder withPosition(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder withColor(int red, int green, int blue) {
            this.red = red;
            this.green = green;
            this.blue = blue;
            return this;
        }

        public Pixel build() {
            return new Pixel(this);
        }
    }

    public Pixel(Builder builder) {
        this.x = builder.x;
        this.y = builder.y;
        this.red = builder.red;
        this.green = builder.green;
        this.blue = builder.blue;
    }

    public int getX() {
        return x;
    }

    public int getY(){
        return y;
    }

    public int getRed() {
        return red;
    }

    public int getGreen() {
        return green;
    }

    public int getBlue() {
        return blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Pixel other = (Pixel) obj;
        return Objects.equals(this.x, other.x)
                && Objects.equals(this.y, other.y);
    }

    @Override
    public String toString() {
        return "Pixel{" +
                "x=" + x +
                ", y=" + y +
                ", red=" + red +
                ", green=" + green +
                ", blue=" + blue +
                '}';
    }

}
