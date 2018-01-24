package bg.unisofia.s81167.application;

import bg.unisofia.s81167.controller.ApplicationController;
import bg.unisofia.s81167.controller.MillionDollarSchmeckelHomepageController;
import bg.unisofia.s81167.image.PngImageCreator;
import bg.unisofia.s81167.persistence.initializer.MySqlPersistenceInitializer;
import bg.unisofia.s81167.persistence.initializer.PersistenceInitializer;
import bg.unisofia.s81167.persistence.pixel.MySqlPixelDataAccessObject;
import bg.unisofia.s81167.persistence.user.MySqlUserDataAccessObject;
import bg.unisofia.s81167.service.MillionSchmeckleHomepageService;
import bg.unisofia.s81167.service.exception.WebApplicationExceptionMapper;
import bg.unisofia.s81167.service.filter.BearerAuthenticationFilter;
import bg.unisofia.s81167.service.reader.JsonBodyReader;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("api/v1")
public class RestApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        return super.getClasses();
    }

    @Override
    public Set<Object> getSingletons() {
        initializeDatabase();
        final MillionSchmeckleHomepageService service = getService();

        final Set<Object> singletons = new HashSet<>();
        singletons.add(service);
        singletons.add(new JsonBodyReader<>());
        singletons.add(new BearerAuthenticationFilter());
        singletons.add(new WebApplicationExceptionMapper());

        return singletons;
    }

    private void initializeDatabase() {
        final PersistenceInitializer initializer = new MySqlPersistenceInitializer();
        initializer.initialize();
    }

    private MillionSchmeckleHomepageService getService() {
        final ApplicationController controller = new MillionDollarSchmeckelHomepageController.Builder()
                .withImageCreator(new PngImageCreator())
                .withUserDao(new MySqlUserDataAccessObject())
                .withPixelDao(new MySqlPixelDataAccessObject())
                .build();

        return new MillionSchmeckleHomepageService(controller);
    }

}
