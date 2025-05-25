package hello.exception.api;

//import hello.exception.exception.BadRequestException;

import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
public class ApiExceptionV3Controller {

    @GetMapping("/api3/members/{id}")
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
