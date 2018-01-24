package bg.unisofia.s81167.image.scheduler;

import bg.unisofia.s81167.image.ImageCreator;
import bg.unisofia.s81167.model.Pixel;
import bg.unisofia.s81167.persistence.pixel.PixelDataAccessObject;
import com.google.common.util.concurrent.AbstractScheduledService;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class GuavaBasedImageUpdateScheduler extends AbstractScheduledService implements ImageUpdateScheduler{

    private final long initialDelay;
    private final long period;
    private final TimeUnit periodTimeUnit;
    private final ImageCreator imageCreator;
    private final PixelDataAccessObject pixelDao;
    private final AtomicReference<byte[]> imageReference;

    public static class Builder {

        private long initialDelay;
        private long period;
        private TimeUnit periodTimeUnit;
        private ImageCreator imageCreator;
        private PixelDataAccessObject pixelDao;
        private AtomicReference<byte[]> imageReference;

        public Builder withInitialDelay(long initialDelay) {
            this.initialDelay = initialDelay;
            return this;
        }

        public Builder withPeriod(long period) {
            this.period = period;
            return this;
        }

        public Builder withPeriodTimeUnit(TimeUnit periodTimeUnit) {
            this.periodTimeUnit = periodTimeUnit;
            return this;
        }

        public Builder withImageCreator(ImageCreator imageCreator) {
            this.imageCreator = imageCreator;
            return this;
        }

        public Builder withPixelDao(PixelDataAccessObject pixelDao) {
            this.pixelDao = pixelDao;
            return this;
        }

        public Builder withImageReference(AtomicReference<byte[]> imageReference) {
            this.imageReference = imageReference;
            return this;
        }

        public GuavaBasedImageUpdateScheduler build() {
            return new GuavaBasedImageUpdateScheduler(this);
        }
    }

    private GuavaBasedImageUpdateScheduler(Builder builder) {
        this.initialDelay = builder.initialDelay;
        this.period = builder.period;
        this.periodTimeUnit = builder.periodTimeUnit;
        this.imageCreator = builder.imageCreator;
        this.pixelDao = builder.pixelDao;
        this.imageReference = builder.imageReference;
    }

    @Override
    public void start() {
        this.startAsync();
    }

    @Override
    public void stop() {
        this.stopAsync();
    }

    @Override
    protected void runOneIteration() throws Exception {
        final Collection<Pixel> pixels = pixelDao.getAllPixels();
        final byte[] image = imageCreator.createImage(pixels);
        imageReference.set(image);
    }

    @Override
    protected Scheduler scheduler() {
        return AbstractScheduledService.Scheduler.newFixedRateSchedule(initialDelay, period, periodTimeUnit);
    }

}
