package bg.unisofia.s81167.controller;

public class InvalidUserCredentials extends Exception {

    public InvalidUserCredentials(String message) {
        super(message);
    }

}
