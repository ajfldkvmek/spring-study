package hello.core.web;

import hello.core.common.MyLogger;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LogDemoController {

    //이게 실행하면 왜 오류가 나냐???

    //이 경우 요청이 와야 빈이 생성되기 떄문에
    //스프링을 실행했을 때 빈이 존재하지 않아서 생기는 오류
    
    //그렇다면 이 처리를 어떻게 해야할까??
    // >>> 이 때 Provider를 사용하는 것이다

//    provider도 귀찮아???
//    그럼 넌 proxyMode야
//
    private final LogDemoService logDemoService;
    private final MyLogger myLogger;
//    private final ObjectProvider<MyLogger> myLoggerProvider;

    //되돌리기  : ctrl + z
    //되되돌   : Ctrl + Shift + Z
    @RequestMapping("log-demo")
    @ResponseBody
    public String logDemo(HttpServletRequest req) throws InterruptedException{

//        MyLogger myLogger = myLoggerProvider.getObject(); - provider
        String requestURL = req.getRequestURL().toString();

        System.out.println("myLogger = " + myLogger.getClass()); //프록시가 가짜 객체를 만들어서 일단 실행함

        myLogger.setRequestURL(requestURL); //사실 이건 인터셉트나 필터로 하는것이 좋다

        myLogger.log("controller test");
        Thread.sleep(1000);
        logDemoService.logic("testId");

        return "OK";
    }
}
