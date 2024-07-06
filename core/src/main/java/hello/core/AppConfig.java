//package hello.core;
//
//import hello.core.discount.DiscountPolicy;
//import hello.core.discount.RateDiscountPolicy;
//import hello.core.member.MemberRepository;
//import hello.core.member.MemberServiceImpl;
//import hello.core.member.MemberService;
//import hello.core.member.MemoryMemberRepository;
//import hello.core.order.OrderService;
//import hello.core.order.OrderServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class AppConfig {
//
//    //히스토리 보기: ctrl + e
//    //리팩터링 단축키: ctrl + alt + m
//
//    //@Bean만 사용하여도 빈등록은 되지만
//    //싱글톤패턴은 적용되지 않음
//    @Bean
//    public MemoryMemberRepository memberRepository() {
//        System.out.println("call AppConfig.memberRepository");
//        return new MemoryMemberRepository();
//    }
//
//    //이렇게 되면 아래와 같이 정책이 바뀌었을 때 이 코드만 수정해주면 된다.
//    @Bean
//    public DiscountPolicy discountPolicy(){
////        return new FixDiscountPolicy();
//        return new RateDiscountPolicy();
//    }
//
//    @Bean
//    public MemberService memberService(){
//        System.out.println("call AppConfig.memberService");
//        return new MemberServiceImpl(memberRepository()); //생성자 주입
//    }
//
//    @Bean
//    public OrderService orderService(){
//        System.out.println("call AppConfig.orderService");
//        return new OrderServiceImpl(memberRepository(), discountPolicy());
//    }
//
//
//}
