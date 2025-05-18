package hello.login.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("logFilter init");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws ServletException, IOException {
        // doFilter 에 로직을 구현하면 된다!
        log.info("logFilter doFilter");

        /**
         *  ServletRequest 는 HttpServletRequest 의 부모 객체이다.
         *  Http 이외에 다양한 요청들을 받을 수 있음!(사용은 거의 안함)
         */
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();

        String uuid = UUID.randomUUID().toString();

        try{
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            // 이렇게 메소드로 받은 filterChain 을 사용하지 않으면 필터체인이 적용되지 않음(매우 중요)
            // filter 를 순서대로 찾아가며 모든 필터체인이 끝나면 서블릿 호출
            // 마지막 필터라고 filterChain.doFilter(servletRequest, servletResponse)하지 않으면
            // 서블릿이 호출되지 않는다.

            // 하지만 의도적으로 필터체인을 적용시기지 않을 수도 있는데 이 때는
            // 그 경우에만 return 을 따로 해주도록 하자(ex. 로그인 페이지로 이동시키기) - 서블릿필터 인증 체크
            filterChain.doFilter(servletRequest, servletResponse);
        } catch(Exception e){
            throw e;
        }finally {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
        }

    }

    @Override
    public void destroy() {
        log.info("logFilter destroy");
    }


}
