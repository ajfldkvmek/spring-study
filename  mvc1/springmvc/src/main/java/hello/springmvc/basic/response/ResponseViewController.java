package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        ModelAndView mav = new ModelAndView("response/hello")
                .addObject("data", "hello!");
        return mav;
    }

    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        model.addAttribute("data", "hello!");
        return "response/hello";
    }

    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello!");
        // /response/hello
        // 아무것도 반환하지 않으면(void) 자기 자신( RequestMapping 에서 설정된 경로)에게 반환함
        //( mapping 경로와 반환주소의 논리경로가 같아야함 )
        // 하지만 추천하지는 않음 (명시적이지 않아)
    }


    /*

    build.gradle
    `implementation 'org.springframework.boot:spring-boot-starter-thymeleaf'`
    스프링 부트가 자동으로 ThymeleafViewResolver 와 필요한 스프링 빈들을 등록한다.
    
    그리고 다음 설정도 사용한다.
    이 설정은 기본 값 이기 때문에 변경이 필요할 때만 설정하면 된다.
    application.properties
    spring.thymeleaf.prefix=classpath:/templates/
    
     여러 thymeleaf 옵션도 설정가능 (  spring 공식페이지 참조 )

     */

}
