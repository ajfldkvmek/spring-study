package hello.springmvc.basic.request;

import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {


    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok"); //getWriter() 사용시 null 값이 올 수 있기 때문에 예외 처리 추가필요
    }




    // @ResponseBody 는 @RestController 과 같은 효과로
    // return 값 자체를 http 응답과 같이 전달한다
    @ResponseBody
    @RequestMapping("/request-param-v2")
    public String requestParamV2(
            @RequestParam("username") String memberName,
            @RequestParam("age") int memberAge
    ) {
        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }



    @ResponseBody
    @RequestMapping("/request-param-v3")
    public String requestParamV3(
            //아래와 같이 요청 파라매터네임과 변수명을 일치시켜 주면
            //생략할 수도 있다.
            @RequestParam String username,
            @RequestParam int age
    ) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }


    @ResponseBody
    @RequestMapping("/request-param-v4")
    public String requestParamV4(
            //단순 타입의 경우 아래처럼 생략할 수도 있다.(@RequestParam 을 자동적으로 해줌)
            //하지만 명확성이 조금 떨어지기 때문에 다른 사람이 볼 때 이해가 잘 가지 않을 수 있다.
            String username,
            int age
    )  {

        log.info("username={}, age={}", username, age);
        return "ok";
    }


    @ResponseBody
    @RequestMapping("/request-param-required")
    public String requestParamRequired(
            //required 의 default 값은 true
            //만약 아래와 같이 true로 설정한 경우 해당 값이 필수적으로 전달 되어야함
            //즉 http 에서 요청을 보낼 때 해당 파라메터로 설정된 값이 없으면 오류가 발생(bad request - 400)

            //################### 주의 ####################
            //int 의 경우 무조건 값이 입력되어야 한다(null 일 경우 오류 발생함)
            //즉 숫자값을 required=false 로 입력받기 위해선는
            //int 가 아니라 Integer 로 받아야한다

            //만약 required=true 인데 아무 값이 안들어 왔을 경우 빈문자로 취급된다
            //이 때문에 null 일 경우와 빈문자("") 값을 모두 체크해주여야함
            //이를 방지하기 위해서는 defaultValue 값을 설정하기도 함


            @RequestParam(required = true) String username,
            @RequestParam(required = true) Integer age
    ) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }


    @ResponseBody
    @RequestMapping("/request-param-default")
    public String requestParamDefault(
            //이렇게 defaultValue 를 설정한 경우 아무 값이 안왔을 경우 자동적으로 default 로 설정한 값이 할당된다
            //따라서 이 경우에은 int 를 사용할 수 있다.
            //또 이경우에는 required를 설정할 필요도 없어진다
            // ################################빈문자의 경우에도 설정한 값이 입력됨
//            @RequestParam(required = true, defaultValue = "guest") String username,
//            @RequestParam(required = true, defaultValue = "-1") int age
            @RequestParam(defaultValue = "guest") String username,
            @RequestParam(defaultValue = "-1") int age
    ) {
        log.info("username={}, age={}", username, age);
        return "ok";
    }



//    parameter 를
//    Map 또는 MultiValueMap
//    으로 설정할 수도 있다
//    parameter 값이 1개가 확실하다면 Map 을 사용해도 되지만
//    그렇지 않을 경우 MultiValueMap 을 사용하자
    
    @ResponseBody
    @RequestMapping("/request-param-map")
    public String requestParamMap(
            @RequestParam Map<String, Object> paramMap
    ) {
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }



    /*
    스프링MVC는 @ModelAttribute 가 있으면 다음을 실행한다.
    HelloData 객체를 생성한다.
    요청 파라미터의 이름으로 HelloData 객체의 프로퍼티를 찾는다. 그리고 해당 프로퍼티의 setter를 호출해서파라미터의 값을 입력(바인딩) 한다.
    예) 파라미터 이름이 username 이면 setUsername() 메서드를 찾아서 호출하면서 값을 입력한다.

    값을 넣을 때 잘못된 데이터타입이 들어갈 경우
    BindException 이 발생한다.
    (데이터 타입과 입력값을 주의해야함) <<< 잘못 입력할 경우 spring 이 잘 도와줌

     */
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(
            @ModelAttribute HelloData helloData
    ) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }


    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(
            // @ModelAttribute 는 생략가능
            // 근데 위에서 사용한 @RequestParam 도 생략이 가능했따.
            // 그럼 스프링에서는 어떻게 처리할까?
            // 단순 타입의 (String, Integer, int) 의 경우에는 RequestParam 으로 알아서 설정함
            // 나머지는 ModelAttribute 로 지정해준다 ( argument resolver 제외) 
            
            //############  argument resolver ###################
            // ex) HttpServletRequest 같은 놈들 (내장 객체들이 여기에 해당한다고 보면 될듯함)
            // ModelAttribute 의 경우 사용자가 생성한 객체(?)
           HelloData helloData
    ) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }


}
