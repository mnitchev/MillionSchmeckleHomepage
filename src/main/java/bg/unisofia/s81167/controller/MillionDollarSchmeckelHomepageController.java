package bg.unisofia.s81167.controller;

import bg.unisofia.s81167.image.ImageCreator;
import bg.unisofia.s81167.image.scheduler.GuavaBasedImageUpdateScheduler;
import bg.unisofia.s81167.image.scheduler.ImageUpdateScheduler;
import bg.unisofia.s81167.model.Pixel;
import bg.unisofia.s81167.model.User;
import bg.unisofia.s81167.persistence.PersistenceException;
import bg.unisofia.s81167.persistence.pixel.PixelDataAccessObject;
import bg.unisofia.s81167.persistence.user.UserDataAccessObject;
import bg.unisofia.s81167.util.Verify;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static bg.unisofia.s81167.util.Verify.verifyNotNull;

public class MillionDollarSchmeckelHomepageController implements ApplicationController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MillionDollarSchmeckelHomepageController.class);

    private ImageCreator imageCreator;
    private UserDataAccessObject userDao;
    private PixelDataAccessObject pixelDao;
    private AtomicReference<byte[]> imageData;

    public static class Builder {

        private UserDataAccessObject userDao;
        private PixelDataAccessObject pixelDao;
        private ImageCreator imageCreator;

        public Builder withUserDao(UserDataAccessObject userDao) {
            this.userDao = userDao;
            return this;
        }

        public Builder withPixelDao(PixelDataAccessObject pixelDao) {
            this.pixelDao = pixelDao;
            return this;
        }

        public Builder withImageCreator(ImageCreator imageCreator) {
            this.imageCreator = imageCreator;
            return this;
        }

        public MillionDollarSchmeckelHomepageController build() {
            return new MillionDollarSchmeckelHomepageController(this);
        }
    }

    private MillionDollarSchmeckelHomepageController(Builder builder) {
        this.pixelDao = builder.pixelDao;
        this.userDao = builder.userDao;
        this.imageCreator = builder.imageCreator;
        initializeImageUpdateScheduler();
    }

    private void initializeImageUpdateScheduler() {
        this.imageData = new AtomicReference<>();
        final ImageUpdateScheduler imageUpdateScheduler = new GuavaBasedImageUpdateScheduler.Builder()
                .withInitialDelay(0)
                .withPeriod(5)
                .withPeriodTimeUnit(TimeUnit.SECONDS)
                .withImageCreator(imageCreator)
                .withImageReference(imageData)
                .withPixelDao(pixelDao)
                .build();
        imageUpdateScheduler.start();
    }

    @Override
    public void allocatePixels(String username, Collection<Pixel> pixels) throws PixelAllocationException {
        verifyParameters(username, pixels);
        final Set<Pixel> pixelSet = new HashSet<>(pixels);
        try {
            for (Pixel pixel : pixelSet) {
                pixelDao.allocatePixel(username, pixel);
            }
        } catch (PersistenceException e) {
            final String message = String.format("Failed to allocate pixel %s, to user %s.", pixels, username);
            LOGGER.error(message, e);
            throw new PixelAllocationException(message, e);
        }
    }

    @Override
    public byte[] getImageData() {
        return imageData.get();
    }

    @Override
    public void registerUser(User user) throws UserAlreadyExistsException {
        verifyNotNull(user);
        verifyNotNull(user.getUsername(), user.getPassword());
        try {
            if (userDao.userExists(user)) {
                final String message = String.format("User with username : %s already exists.", user.getUsername());
                throw new UserAlreadyExistsException(message);
            }
            userDao.registerUser(user);
        } catch (PersistenceException e) {
            final String message = String.format("Failed to register user with username : %s.", user.getUsername());
            LOGGER.error(message, e);

            throw new MillionSchmeckleHomepageException(message, e);
        }
    }

    @Override
    public void authenticateUser(User user) throws InvalidUserCredentials {
        verifyNotNull(user);
        verifyNotNull(user.getUsername(), user.getPassword());
        try {
            if (!userDao.userHasValidCredentials(user)) {
                LOGGER.debug("User %s has provided invalid credentials.", user.getUsername());
                throw new InvalidUserCredentials("Wrong username or password.");
            }
        } catch (PersistenceException e) {
            final String message = String.format("Failed to verify user credentials for user : %s",
                    user.getUsername());
            LOGGER.error(message);

            throw new MillionSchmeckleHomepageException(message, e);
        }
    }

    @Override
    public boolean userHasToken(String username) {
        verifyNotNull(username);
        try {
            return userDao.userHasToken(username);
        } catch (PersistenceException e) {
            throw new MillionSchmeckleHomepageException(e);
        }
    }

    @Override
    public String getToken(String username) {
        verifyNotNull(username);
        try {
            return userDao.getUserToken(username);
        } catch (PersistenceException e) {
            throw new MillionSchmeckleHomepageException(e);
        }
    }

    @Override
    public void persistUserToken(String username, String token) {
        try {
            userDao.addUserToken(username, token);
        } catch (PersistenceException e) {
            throw new MillionSchmeckleHomepageException(e);
        }
    }

    private void verifyParameters(String username, Collection<Pixel> pixels) {
        verifyNotNull(username);
        verifyNotNull(pixels.toArray());
    }

}
