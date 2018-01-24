package bg.unisofia.s81167.persistence.user;

public enum UsersPreparedStatements {
    CREATE_TABLE("CREATE TABLE %s (username VARCHAR(25) PRIMARY KEY, password VARCHAR(20))"),
    CREATE_TOKEN_TABLE("CREATE TABLE %s (username VARCHAR(25) PRIMARY KEY, token VARCHAR(64), insertion DATETIME," +
            "CONSTRAINT FOREIGN KEY (username) REFERENCES Users(username))"),
    GET_USER("SELECT * FROM %s WHERE username = ?"),
    GET_TOKEN("SELECT * FROM %s WHERE token = ?"),
    INSERT_USER("INSERT INTO %s VALUES (?, ?)"),
    INSERT_TOKEN("INSERT INTO %s VALUES(?, ?, NOW())"),
    GET_USER_TOKEN("SELECT * FROM %s WHERE username = ?"),
    DELETE_OLD_TOKENS_EVENT("CREATE EVENT AutoDeleteOldTokens " +
            "ON SCHEDULE EVERY 10 MINUTE " +
            "DO " +
            "DELETE FROM %s WHERE insertion < DATE_SUB(NOW(), INTERVAL 10 MINUTE)");

    private final String statement;

    UsersPreparedStatements(String statement) {
        this.statement = statement;
    }

    public String getStatement(Object... templateValues){
        return String.format(statement, templateValues);
    }

}
