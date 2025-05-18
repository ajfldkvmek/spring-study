package hello.login.web.filter;

import hello.login.web.session.SessionConst;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    /**
     *
     *  참고로 최근 서블릿 스팩에서는 init 과 destroy 의 경우
     *  default 로 기본설정이 되어 있기 때문에
     *  필수적으로  구현해야할 메소드는 doFilter 하나이다.
     */
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        Filter.super.init(filterConfig);
//    }
//
//    @Override
//    public void destroy() {
//        Filter.super.destroy();
//    }

    // 로그인 하지 않고 접근 가능한 리소스들을 관리하기 위한 화이트리스트 생성
    private static final String[] whiteList = {"/", "/member/add", "/login", "/logout", "/css/*", "/images/*"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        String requestURI = httpRequest.getRequestURI();
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        try {
            log.info("인증 체크 필터 시작{}", requestURI);

            if(isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행{}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if(session == null ||
                        session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 {}" , requestURI);
                    // 인증 체크 회원가 없음 -> 로그인 페이지로 이동
                    // redirectURL=" + requestURI 로그인 성공후 이전 페이지로 하기 위한 쿼리스트링
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return;
                }
            }

            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            log.error("필터 처리 중 오류", e);
            throw e; // 예외 로깅 가능하지만 톰캣까지 예외를 보내주어야함
        } finally {
            log.info("인증 체크 필터 종료");
        }
    }


    /**
     *  화이트 리스트의 경우 인증 체크 하지 않음
     */
    private boolean isLoginCheckPath(String requestURI) {
        // 스프링 제공 PatternMatchUtils.simpleMatch() 메소드 사용
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }
}
