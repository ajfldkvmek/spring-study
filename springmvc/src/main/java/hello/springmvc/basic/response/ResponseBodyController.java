package hello.springmvc.basic.response;


import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@Controller
//@ResponseBody 처럼 클래스에 붙일 경우 전체 메소드에 대해 ResponseBody 가 붙게됨
//@RestController <<< 얘는 Controller 과 ResponseBody 어노테이션을 합친 놈 그래서 얘를 더 많이 씀
//Rest 는 RestAPI 할 때 그 Rest 임
//이놈을 만들 때 얘를 많이 사용해
public class ResponseBodyController {


    @GetMapping("/response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }


    @GetMapping("/response-body-string-v2")
    public ResponseEntity<String> responseBodyV2(HttpServletResponse response) {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    @ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyV3() {
        return "ok";
    }


    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1(){
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(10);

        return new ResponseEntity<>(helloData, HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2(){
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(10);

        //이 경우 메세지를 바꿀 수 가 없기 떄문에 ResponseStatus 어노테이션을 추가해서 사용함
        //하지만 이렇게 사용할 경우 상태메세지가 항상고정되기 때문에
        //경우에 따라 다르게 보내고 싶을 경우는 ResponseEntity 를 사용하자
        return helloData;
    }







}
