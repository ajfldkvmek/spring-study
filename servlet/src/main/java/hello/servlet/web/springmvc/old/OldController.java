package hello.servlet.web.springmvc.old;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

@Component("/springmvc/old-controller") //spring bean 의 이름을 설정한 것
public class OldController implements Controller {

    @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        System.out.println("OldController.handleRequest");
        //이 경우 호출은 되지만 뷰리졸버가 없기에 오류가 나게된다
        //스프링프레임워크에서 xml파일에 prefix, suffix를 지정한 것 처럼 스프링부트에서도 그 설정이 필요
        //설정은 application.properties에서 한다.
        //이렇게 설정을 해주면 스프링부트에서는 InternalResourceViewResolver 라는 뷰 리졸버를 자동으로 등록한다
//      spring.mvc.view.prefix=/WEB-INF/views/
//      spring.mvc.view.suffix=.jsp

        return new ModelAndView("new-form");
    }
}
