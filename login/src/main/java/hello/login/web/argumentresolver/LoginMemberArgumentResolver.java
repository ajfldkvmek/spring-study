package hello.login.web.argumentresolver;

import hello.login.domain.member.Member;
import hello.login.web.session.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행 = {}", parameter.getParameterType());

        // @Login 어노테이션이 붙어 있고
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
        // 멤버 타입이냐
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());

        return hasMemberType && hasLoginAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false);

        if(session == null){
            return null;
        }

//        Object member = session.getAttribute(SessionConst.LOGIN_MEMBER);
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
