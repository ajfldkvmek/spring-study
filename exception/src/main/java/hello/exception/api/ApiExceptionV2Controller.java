package hello.exception.api;

//import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
//import hello.exception.exhandler.ErrorResult;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionV2Controller {

    /**
     *  @ExceptionHandler 어노테이션은
     *  해당 컨트롤러 안에서 발생된 예외만을 처리해준다
     *
     * @RestController 말고
     * @Controller 어노테이션의 경우도 가능하다. but 이 경우는 잘 사용하지 않음
     *
     * 근데 만약 여러 컨트롤러에서 동일한 오류처리를 하고싶으면??
     * -> @ControllerAdvice 어노테이션을 사용할 수 있다. (-> ExControllerAdvice 참고)
     *
     */

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ErrorResult illegalArgumentException(IllegalArgumentException e) {
//        log.error("[exceptionHandler] ex", e);
//        // 이 경우 기본적으로 정상 흐름으로 바뀌어 http 상태코드가 200이 된다
//        // 따라서 원하는 상태코드를 변경시켜주려면 @ResponseStatus 어노테이션으로 설정하면 된다.
//        // ExceptionHandlerExceptionResolver 에서 처리가 완료됐기 때문에
//        // servletcontainer 까지 가지 않고 통신이 완료된다.(정상흐름으로 처리)
//        return new ErrorResult("BAD", e.getMessage());
//    }
//
//    @ExceptionHandler
//    public ResponseEntity<ErrorResult> userHandler(UserException e) {
//        log.error("[exceptionHandler] ex", e);
//        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
//        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
//    }
//
//    /**
//     * 하위 Exception 들은 전부 잡을 수 있다.
//     * 더 상세하게 정의된 에러클래스가 없으면 여기서 전부 잡음(ex. RuntimeException )
//     */
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    @ExceptionHandler
//    public ErrorResult exHandler(Exception e) {
//        log.error("[exceptionHandler] ex", e);
//        return new ErrorResult("EX", "내부 오류");
//    }


    @GetMapping("/api2/members/{id}")
    public MemberDto getMember(@PathVariable("id") String id) throws Exception {

        if (id.equals("ex")) {
            throw new RuntimeException("잘못된 사용자");
        }

        if(id.equals("bad")) {
            throw new IllegalArgumentException("잘못된 입력 값");
        }

        if (id.equals("user-ex")) {
            throw new UserException("사용자 정의 예외");
        }

        return new MemberDto(id, "hello" + id);
    }

//    @GetMapping("/api2/response-status-ex1")
//    public String responseStatusEx1() {
//        throw new BadRequestException();
//    }
//
//    @GetMapping("/api2/response-status-ex2")
//    public String responseStatusEx2() {
//        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
//    }
//
//
//    @GetMapping("/api2/default-handler-ex")
//    public String defaultException(@RequestParam Integer data) {
//        return "ok";
//    }


    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
