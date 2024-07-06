package hello.core.member;

import hello.core.AppConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


public class MemberServiceTest {

    //이렇게 되면 테스트 케이스도 수정이 필요하다
    MemberService memberService;

    @BeforeEach
    public void boforeEach() {
        AppConfig appConfig = new AppConfig();
        memberService = appConfig.memberService();
    }

    @Test
    void join() {

        //given
        Member member = new Member(1L, "memberA", Grade.VIP);

        //whne
        memberService.join(member);
        Member findMember = memberService.findMember(1L);

        //then
        Assertions.assertThat(member).isEqualTo(findMember);
    }

}
