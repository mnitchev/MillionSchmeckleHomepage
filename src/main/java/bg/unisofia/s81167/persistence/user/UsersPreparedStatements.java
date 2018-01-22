package bg.unisofia.s81167.persistence.user;

public enum UsersPreparedStatements {
    CREATE_TABLE("CREATE TABLE %s (username VARCHAR(25) PRIMARY KEY, password VARCHAR(64))"),
    GET_USER("SELECT * FROM %s WHERE username = ?"),
    INSERT_USER("INSERT INTO %s VALUES (?, ?)");

    private final String statement;

    UsersPreparedStatements(String statement) {
        this.statement = statement;
    }

    public String getStatement(Object... templateValues){
        return String.format(statement, templateValues);
    }

}
