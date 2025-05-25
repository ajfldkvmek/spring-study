package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;


/**
 *  스프링부트에서는 BasicErrorController 의 기본적인 로직이 모두 개발되어있다.
 *  개발자는 오류페이지 화면만 BasicErrorController 가 제공하는 룰과 우선순위에 따라 등록하면된다.
 *
 *  1. 뷰 템플릿        : resource/templates/error/500.html
 *  2. 정적 리소스       : resource/static/error/500.html
 *  3. 적용대사이 없을    : resource/templates/error.html
 *
 *  과 같이 파일을 등록해서 관리할 수 있다.
 *
 *  404같이 구체적인것이 4xx처럼 덜 구체적인 것 보다 우선순위가 높다
 *  ps. 예외는 500 에러로 처리
 *
 *  스프링에서 기본제공되는 에러페이지 관리 컨트롤러 말고 커스텀하기 위해선 아래와 같이 생성 후 @Component 어노테이션을 추가하도록 하자
 *
 *  하지만 BasicErrorController 에는 오류정보를 노출하기 때문에 이를 참조하고 적용하자
 *  (노출하지 않는 것이 좋다 model 정보에 담을지 말지 설정할 수 있다! - application.properties 내용참조)
 *
 *  *********
 *  BasicErrorController 에서 제공하는 기본 오류는 HTML 페이지를 제공하는 경우에는 편리하지만 API 오류를 처리함에는 조금 다르다.
 *  API 오류처리는 각 컨트롤러나 예외마다 다르게 응답결과를 출력해야 할 수도 있다.
 *  결과에 따라 다른 오류를 처리하고 싶을때, 이를 위해 @ExceptionHandler 어노테이션을 통해 처리할 수 있다.
 */

//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");

        // RuntimeException 의 자식 예외는 전부 전달된다
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
    }
}
