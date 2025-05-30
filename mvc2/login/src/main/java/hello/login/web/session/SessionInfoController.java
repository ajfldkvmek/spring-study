package hello.login.web.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 *  세션 타임아웃 설정
 *  세션은 기본적으로 메모리에 저장된다.
 *  즉 10만명이 요청하면 세션이 10만개 생성됨
 *
 *  그렇다면 이 세션의 유지시간이 제한이 없다면 어떻게 될까?
 *
 *  그렇다고 일정시간마다 세션을 모두 제거하면 매번 해당 시간이 지나면 새로 로그인하는 번거로움이 발생한다.
 *
 *  -> 생성된 시간이 아니라 사용자가 서버에 최근에 요청한 시간을 기준으로 세션의 수명을 늘리면 된다.
 *  application.properties 에서
 *  server.servlet.session.timeout = 60 과 같이 설정하면된다.(60초) - 글로벌 설정은 분단위로 설정하기
 *  특정 세션에 대해서 따로 관리하고 싶을 경우
 *  세션 생성시
 *  session.setMaxInactiveInterval(1000); // 와 같이 초단위로 설정할 수 있다.
 *
 */

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {

        HttpSession session = request.getSession();
        if (session == null) {
            return "session is null";
        }

        // 세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name = {}, value = {}", name, session.getAttribute(name)));

        log.info("sessionId={}", session.getId());
        log.info("maxInactiveInterval={}", session.getMaxInactiveInterval());
        log.info("creationTime={}", new Date(session.getCreationTime()));
        log.info("lastAccessedTime={}", new Date(session.getLastAccessedTime()));
        log.info("isNew={}", session.isNew());

        return "세션 출력";
    }
}
