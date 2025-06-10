package hello.jdbc.connection;

// 객체생성 막기 위해 abstract 선언
public abstract class ConnectionConst {

    /**
     * JDBC 연결 규약
     */
    // h2 DB connection
    public static final String URL = "jdbc:h2:tcp://localhost/~/test";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";

}
