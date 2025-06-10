package hello.jdbc.exception.basic;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
public class CheckedTest {

    @Test
    void checked_catch() {
        Service service = new Service();
        service.callCatch();
    }

    // 테스트 에서는 throw 가 발생하면 실패한 것으로 처리된다.
    // 따라서 해당 메소드를 호출했을 때
    // Assertions.assertThatThrownBy 를 통해 호출되는 예외를 체크하는 방식으로 테스트를 해야한다.
    // throw 예외는 Exception 같이 포괄적인 예외를 던지지말말고 명시적으로 정하도록 하자
    @Test
    void checked_throw()  {
        Service service = new Service();
        Assertions.assertThatThrownBy(() -> service.callThrow())
                .isInstanceOf(MyCheckedException.class);
    }


    /**
     *  Exception 을 상속받은 예외는 체크 예외가 된다.
     */
    static class MyCheckedException extends Exception {
        public MyCheckedException(String message) {
            super(message);
        }
    }

    static class Service {
        Repository repository = new Repository();

        /**
         *  예외를 잡아서 처리하는 코드
         */
        public void callCatch() {
            try {
                repository.call();
            } catch (MyCheckedException e) {
                // 예외 처리 로직
                // e.getMessate()  : 출력파라메터
                // e: stacktrace
                log.info("예외처리, message = {}", e.getMessage(), e);
            }
        }

        /**
         *  체크 예외를 잡지않고 밖으로 던지기
         *  throw 예외를 메서드에 필수로 선언해야함
         * @throws MyCheckedException
         */
        public void callThrow() throws MyCheckedException {
            repository.call();
        }
    }

    static class Repository {
        public void call() throws MyCheckedException {
            throw new MyCheckedException("Ex");
        }
    }
}
