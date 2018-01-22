package bg.unisofia.s81167.persistence.user;

import bg.unisofia.s81167.model.User;
import bg.unisofia.s81167.persistence.PersistenceException;

public interface UserDataAccessObject {

    boolean userExists(User user) throws PersistenceException;

    boolean userHasValidCredentials(User user) throws PersistenceException;

    void registerUser(User user) throws PersistenceException;

}
