package hello.exception.api;


import hello.exception.exception.BadRequestException;
import hello.exception.exception.UserException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ApiExceptionController {

    @GetMapping("/api/members/{id}")
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

     @GetMapping("/api/response-status-ex1")
     public String responseStatusEx1() {
         throw new BadRequestException();
     }

    @GetMapping("/api/response-status-ex2")
    public String responseStatusEx2() {
        //ResponseStatusException(HTTP 상태코드, 에러메세지, 실제 발생한 오류);
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "error.bad", new IllegalArgumentException());
    }


    @GetMapping("/api/default-handler-ex")
    public String defaultException(@RequestParam Integer data) {
        return "ok";
    }


    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String memberId;
        private String name;
    }
}
