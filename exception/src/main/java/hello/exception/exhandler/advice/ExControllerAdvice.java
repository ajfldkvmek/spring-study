package hello.exception.exhandler.advice;

import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 *  RestControllerAdvice 에서 대상을 적용하지 않으면
 *  어떤 컨트롤러에서 올라와도 이 ControllerAdvice 가 호출됨
 *
 *  대상의 경우에는
 *  annotations 를 기준으로 하거나 @ControllerAdvice(annotations = RestController.class)
 *  패키지를 기준으로 하거나   @ControllerAdvice("hello.exception.api") - 하위패키지와 클래스 전부 적용됨
 *  직접 클래스를 선택해서  @ControllerAdvice(assignableTypes = { ApiExceptionV2Controller.class, ... })
 *  지정할 수 있다.
 *  => AOP 와 유사함
 *
 */
@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illegalArgumentException(IllegalArgumentException e) {
        log.error("[exceptionHandler] ex", e);
        // 이 경우 기본적으로 정상 흐름으로 바뀌어 http 상태코드가 200이 된다
        // 따라서 원하는 상태코드를 변경시켜주려면 @ResponseStatus 어노테이션으로 설정하면 된다.
        // ExceptionHandlerExceptionResolver 에서 처리가 완료됐기 때문에
        // servletcontainer 까지 가지 않고 통신이 완료된다.(정상흐름으로 처리)
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userHandler(UserException e) {
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    /**
     * 하위 Exception 들은 전부 잡을 수 있다.
     * 더 상세하게 정의된 에러클래스가 없으면 여기서 전부 잡음(ex. RuntimeException )
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandler(Exception e) {
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }

}
