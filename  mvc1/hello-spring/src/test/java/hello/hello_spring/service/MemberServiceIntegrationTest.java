package hello.hello_spring.service;

import hello.hello_spring.domain.Member;
import hello.hello_spring.repository.MemberRepository;
import hello.hello_spring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


/*
 @SpringBootTest: 스프링 컨테이너와 테스트를 함께 실행한다.

 @Transactional 어노테이션을 사용하면 
 테스트 전에 트랜잭션 실행후 
 db에 넣은 데이터를 rollback해준다

 OpenJDK 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
 메세지는 오류가 아니라 단순한 경고다

 */
@SpringBootTest
@Transactional
public class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    /*
        테스트는 메소드명 한글로 적어도 상관없어

        테스트 케이스를 할 때 기본적으로
        given -> when -> then 형식으로 하는 걸 추천
     */
    @Test
    void 회원가입() {
        //given
        Member member = new Member();
        member.setName("spring");

        //when
        Long saveId = memberService.join(member);

        //then
        Member fincMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(fincMember.getName());
    }

    @Test
    public void 중복회원예외(){
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when\
        memberService.join(member1);
        //이렇거ㅔ try catch 하는 거 보다
        /*try {
            memberService.join(member2);
            fail();
        } catch(IllegalStateException e){
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다");
        }*/
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member2));

        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다");

        //then
    }


}
