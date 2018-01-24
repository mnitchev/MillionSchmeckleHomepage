package bg.unisofia.s81167.service.reader;

import java.lang.reflect.Field;

public class NullOrEmptyFieldValidator {

    public void validateObject(Object object) {
        final Field[] fields = object.getClass().getFields();
        for (Field field : fields) {
        }
    }

}
