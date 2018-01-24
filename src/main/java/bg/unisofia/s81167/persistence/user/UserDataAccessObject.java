package bg.unisofia.s81167.persistence.user;

import bg.unisofia.s81167.model.User;
import bg.unisofia.s81167.persistence.PersistenceException;

public interface UserDataAccessObject {

    boolean userExists(User user) throws PersistenceException;

    boolean userHasValidCredentials(User user) throws PersistenceException;

    void registerUser(User user) throws PersistenceException;

    boolean userHasToken(String username) throws PersistenceException;

    void addUserToken(String username, String token) throws PersistenceException;

    String getUserToken(String username) throws PersistenceException;

    boolean tokenValid(String token) throws PersistenceException;
}
