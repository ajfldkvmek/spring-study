package hello.exception.exhandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 단순 html 화면을 제공할 때는 오류가 발생하면 BasicErrorController 를 사용하는게 편하다.
 * <p>
 * 하지만 API는 각 시스템 마다 스펙도 응답의 모양도 모두 다르다.
 * <p>
 * 예외 상황에 따라 각기 다른 데이터를 출력할수도, 다른 로직을 처리해햐할 수 도 있다.
 * <p>
 * 스프링에서는 이런 API 에러 처리 문제를 해결하기 위해 @ExceptionHandler 라는 어노테이션을 사용하는
 * 예외처리 기능을 제공하는데 이것이 바로 ExceptionHandlerExceptionResolver 이다.
 * 스프링은 ExceptionHandlerExceptionResolver 를 기본적으로 제공하고
 * 이는 스프링이 기본적으로 제공하는 ExceptionResolver 중에 우선순위도 가장 높다
 * 실무에서 API 예외처리는 대부분 이 기능을 사용한다
 * <p>
 * like @ExceptionHandler(MyCustomException.class) 어노테이션
 */

//바뀐 점이 있다면?
//Spring WebFlux (Reactive) 환경은 ExceptionHandlerExceptionResolver 대신
//다른 방식(HandlerStrategies, WebExceptionHandler)을 사용하지만, WebMVC에서는 여전히 이 방식이 표준.
//
//Record를 활용한 응답 객체, @ResponseStatusCode의 활용 증가 등 자바/스프링 기능이 발전했지만, 핵심은 동일.

@Data
@AllArgsConstructor
public class ErrorResult {
    private String code;
    private String message;
}
