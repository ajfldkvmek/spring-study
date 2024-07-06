package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//@Controller 룰 사용하면 String return시 뷰 이름이 반환되지만
@RestController //를 사용하면 문자 그 자체가 반환됨
//@Slf4j //lombok제공 로그 어노테이션
public class LogTestController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/log-test")
    public String logTest(){
        String name = "spring";
//        log.trace("info log = " + name); 처럼 사용하지는 마라
//        위와 같이 사용할 경우 연산을 사용하기 때문에 메모리 낭비가 생긴다


        //log level 설정
        log.trace("trace log = {}", name);
        log.debug("debug log = {}", name);
        log.info("info log = {}", name);
        log.warn("warn log = {}", name);
        log.error("error log = {}", name);

        return "ok";
    }

}
