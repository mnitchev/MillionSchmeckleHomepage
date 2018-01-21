package bg.unisofia.s81167.model;

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
