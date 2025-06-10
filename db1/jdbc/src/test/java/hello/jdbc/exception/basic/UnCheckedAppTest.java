package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
public class UnCheckedAppTest {

    @Test
    void checked() {
        Controller controller = new Controller();
        assertThatThrownBy(()->controller.request())
                .isInstanceOf(Exception.class);

    }


    @Test
    void printEx() {
        Controller controller = new Controller();
        try {
            controller.request();
        } catch (Exception e) {
//            e.printStackTrace(); -> 이렇게 하는거 안좋음
            // 로그 마지막 파라메터로 예외를 던져주면 stacktrace 로 출력이 가능하다
            log.info("ex", e);

        }
    }

    static class Controller {

        Service service = new Service();

        public void request() throws SQLException, ConnectException {
            service.logic();
        }

    }

    static class Service {
        Repository repo = new Repository();
        NetworkClient networkClient = new NetworkClient();

        public void logic() {
            repo.call();
            networkClient.call();
        }
    }

    static class NetworkClient {
        public void call() {
            throw new RuntimeConnectException("연결 실패");
        }
    }

    static class Repository {

        void call()  {
            try {
                runSql();
            } catch (SQLException e) {
                throw new RuntimeSQLException(e);
            }
        }

        private void runSql() throws SQLException {
            throw new SQLException();
        }
    }

    static class RuntimeConnectException extends RuntimeException {
        public RuntimeConnectException(String message) {
            super(message);
        }
    }

    static class RuntimeSQLException extends RuntimeException {
        public RuntimeSQLException() {
        }
        public RuntimeSQLException(Throwable cause) {
            super(cause);
        }
    }

}
