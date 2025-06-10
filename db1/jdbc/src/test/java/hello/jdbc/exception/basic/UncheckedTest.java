package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

@Slf4j
public class UncheckedTest {


    @Test
    void unchecked_throw()  {
        Service service = new Service();
        service.callCatch();
    }

    @Test
    void checked_throw()  {
        Service service = new Service();
        Assertions.assertThatThrownBy(()->service.callThrow())
                        .isInstanceOf(MyUncheckedException.class);
    }

    /**
     *  RuntimeException 을 상속받은 예외는 언체크예외가 된다]
     *
     *  uncheckedException 은 throw 생략이 가능하다.
     */
    static class MyUncheckedException extends RuntimeException {
        public MyUncheckedException(String message) {
            super(message);
        }
    }


    /**
     * unchecked 예외는
     * 예외를 잡거나 던지지 않아도 된다.
     * 예외처리를 하지 않을 경우 알아서 throw 해준다!
     */
    static class Service {
        Repository repository = new Repository();

        /**
         * 필요한 경우 잡아서 처리
         */
        public void callCatch() {
            try {
                repository.call();
            } catch (MyUncheckedException e) {
                // 예외 처리 로직
                log.info("예외 처리, message = {}", e.getMessage(), e);
            }
        }

        // 예외 잡지 않아도  상위로 전달!
        // checked 예외 와는 다르게 예외 선언을 하지 않아도 된다
        public void callThrow() {
            repository.call();
        }
    }

    static class Repository {
        public void call() {
            throw new MyUncheckedException("ex");
        }
    }
}
