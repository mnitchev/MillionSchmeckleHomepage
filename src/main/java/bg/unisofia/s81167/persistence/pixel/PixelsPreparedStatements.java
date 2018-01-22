package bg.unisofia.s81167.persistence.pixel;

public enum PixelsPreparedStatements {
    CREATE_TABLE("CREATE TABLE %s (username VARCHAR(25), x INT," +
            "y INT, red INT, green INT, blue INT, insertion DATETIME, CONSTRAINT PRIMARY KEY (x, y)," +
            "CONSTRAINT FOREIGN KEY (username) REFERENCES Users(username)," +
            "CHECK (red >= 0 AND red <= 255 AND " +
            "green >= 0 AND green <= 255 AND " +
            "blue >= 0 AND blue <= 255))"),
    GET_ALL_PIXELS("SELECT * FROM %s"),
    GET_PIXEL("SELECT x, y FROM %s WHERE x = ? AND y = ?"),
    INSERT_PIXEL("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?, NOW())"),
    DELETE_OLD_EVENT("CREATE EVENT AutoDeleteOldPixels " +
            "ON SCHEDULE EVERY 1 MINUTE " +
            "DO " +
            "DELETE FROM %s WHERE insertion < DATE_SUB(NOW(), INTERVAL 30 SECOND)");

    private final String statement;

    PixelsPreparedStatements(String statement) {
        this.statement = statement;
    }

    public String getStatement(Object... templateValues){
        return String.format(statement, templateValues);
    }

}
