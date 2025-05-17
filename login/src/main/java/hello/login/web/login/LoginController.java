package hello.login.web.login;

import hello.login.domain.login.LoginService;
import hello.login.domain.member.Member;
import hello.login.web.session.SessionConst;
import hello.login.web.session.SessionManager;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm loginForm) {
        return "login/loginForm";
    }


    @PostMapping("/login")
    public String loginV3(@Valid @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult,
                          HttpServletResponse response ,
                          HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공 처리
        // 서블릿 제공 세션을 통해 세션 생성
        // 없으면 신규생성, 있으면 해당 세션 반환( default true )
        // false 로 설정시 새로운 세션 생성x(null 반환)
        HttpSession session = request.getSession(true);
        // 세션에 정보 저장
        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);

        return "redirect:/";
    }


//    @PostMapping("/login")
    public String loginV2(@Valid @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult,
                        HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공 처리
        // 세션 관리자(직접구현) 통해 세션 생성하고 회원데이터 보관
        sessionManager.createSession(loginMember, response);

        return "redirect:/";
    }

//    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("loginForm") LoginForm loginForm, BindingResult bindingResult,
                        HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            return "login/loginForm";
        }

        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if (loginMember == null) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공 처리
        // 쿠키에 시간정보를 주지 않으면 세션쿠키(부라우저 종료시 모두 종료)
        Cookie memberId = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(memberId);

        /*
            하지만 쿠키의 경우 클라이언트에서 쿠키를 변경해서 서버에 전달할 수 있다.
            또 쿠키의 정보는 탈취가 매우 쉽다.
            탈취나 위변조가 매우 쉽기 때문에 중요한 정보는(ex. 계정정보) 쿠키에 저장하면 안됨
         */
        return "redirect:/";
    }


//    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        expireCookie(response, "memberId");
        return "redirect:/";
    }

//    @PostMapping("/logout")
    public String logoutV2(HttpServletRequest request) {
        sessionManager.expireCookie(request);
        return "redirect:/";
    }

    @PostMapping("/logout")
    public String logoutV3(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null){
            session.invalidate();
        }
        return "redirect:/";
    }

    private static void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie memberId = new Cookie(cookieName, null);
        // cookie 삭제하려면 시간을 0으로 설정
        memberId.setMaxAge(0);
        response.addCookie(memberId);
    }
}
