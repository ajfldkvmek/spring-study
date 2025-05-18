package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

/**
 *  Tracking Modes
 *  로그인을 처음 시도하면 jsessionId 가 주소 뒤에 출력되는 것을 확인할 수 있다.
 *  이는 웹브라우저가 쿠키를 지원하지 않을 경우 쿠키 대신 url 을 통해 세션을 유지하는 방법이다.
 *
 *  이 방법을 사용하려면 url 에 계속해서 jsessionId 값을 포함해야한다.
 *
 *  서버 입장에서는 브라우저가 쿠키를 지원하는지 판단하지 못하므로 쿠키값과 함께 url 에 값을 전달하는 것이다.
 *
 *  이것을 막으려면 application.properties 에 옵션값을 추가하면된다.
 *  server.servlet.session.tracking-modes = cookie
 *
 */

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId, Model model) {

        if (memberId == null) {
            return "home";
        }

        // 로그인 성공
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) {
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }


//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model){

        //세션 관리자에 저장된 세션 조회
        Member member = (Member) sessionManager.getSession(request);

        if (member == null) {
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }


//    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model){

        // 세션 관리자에 저장된 세션 조회
        HttpSession session = request.getSession(false);

        if (session == null) {
            return "home";
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);

        // 세션에 회원 데이터가 없으면 home 이동
        if(loginMember == null){
            return "home";
        }

        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    /**
     * 스프링 어노테이션을 통한 세션 관리
     * @param loginMember
     * @param model
     * @return
     */
//    @GetMapping("/")
    public String homeLoginV3Spring(
            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
            Model model){

        // 세션에 회원데이터 없으면 home
        if (loginMember == null) {
            return "home";
        }

        // 세션 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(
            @Login Member loginMember,
            Model model){

        // 세션에 회원데이터 없으면 home
        if (loginMember == null) {
            return "home";
        }

        // 세션 유지되면 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }

}