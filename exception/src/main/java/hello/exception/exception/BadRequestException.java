package hello.exception.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 *  기본적으로 Exception 이 발생하면 500 에러가 발생함
 */
///@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "잘못된 오류 요청")  - reason 은 옵션
// reason 의 값으로는 messages.properties 파일에 정의한 구문을 사용할 수도 있다
// 추가로 ResponseStatus 는 개발자가 추가할 수 없는 경우에는 사용할 수 없다. - ex. 라이브러리
// 이 때 ResponseStatusException (스프링제공) 을 사용하면 된다. ApiExceptionController.responseStatusEx2() 참고
// + DefaultHandlerExceptionResolver 를 통해 스프링 내부에서 예외처리를 할 수도 있다
@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException {


}
