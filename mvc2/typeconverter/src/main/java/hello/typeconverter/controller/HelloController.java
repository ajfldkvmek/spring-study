package hello.typeconverter.controller;

import hello.typeconverter.type.IpPort;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HelloController {

    @GetMapping("/hello-v1")
    public String helloV1(HttpServletRequest request) {
//        request.getParmeter()의 경우 기본적으로 문자열로 인식한다.
        String data = request.getParameter("data"); //문자 타입 조회
        Integer intValue = Integer.parseInt(data); // 숫자 타입으로 변경
        System.out.println(intValue);
        return "ok";
    }

    @GetMapping("/hello-v2")
    public String helloV2(@RequestParam Integer data) {
        System.out.println(data);
        return "ok";
    }

    @GetMapping("/ip-port")
    public String ipPort(@RequestParam IpPort ipPort) {
        log.info("ipPort Ip: {}, IpPort Port: {}", ipPort.ip, ipPort.port);
        return "ok";
    }

}
