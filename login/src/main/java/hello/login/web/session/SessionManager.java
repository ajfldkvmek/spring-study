package hello.login.web.session;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    public static final String MY_SESSION_NAME = "mySessionId";
    // 동시성 문제를 위한 concurrentHashMap
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();

    /**
     * 세션 생성
     *
     * @param value
     * @param response
     */
    public void createSession(Object value, HttpServletResponse response) {

        // 세션 아이디 생성하고 저장
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, value);

        // 쿠키 생성
        Cookie mySessionCookie = new Cookie(MY_SESSION_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {
        Cookie sessionCookies = findCookie(request, MY_SESSION_NAME);
        if (sessionCookies == null) {
            return null;
        }
        return sessionStore.get(sessionCookies.getValue());
    }

    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        if (request.getCookies() == null) {
            return null;
        }
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }

    /**
     *  세션 만료
     */
    public void expireCookie(HttpServletRequest request){
        Cookie sessionCookie = findCookie(request, MY_SESSION_NAME);
        if(sessionCookie != null){
            sessionStore.remove(sessionCookie.getValue());
        }
    }
}