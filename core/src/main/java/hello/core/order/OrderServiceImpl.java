package hello.core.order;

import hello.core.annotation.MainDiscountPolicy;
import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//@Component
@Getter
@Setter
@RequiredArgsConstructor // 필수값(final)이 붙은 값의 생성자를 자동으로 만들어줌 - 완벽한 건 아니라 필요할 떄는 생성자 직접생성 해야함
public class OrderServiceImpl implements OrderService {

    //private final MemberRepository memberRepository = new MemoryMemberRepository();
    //이렇게 하여도 ocp, dip를 다 지키지 못한것
//    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();
//    private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    //그럼 얶덯게 하????
    //>>> 아래와 같이 인터페이스에만 의존하도록 한 후
    //누군가가 클라이언트인 OrderServiceImpl, DiscountPolicy의 구현 객체를
    //대신 생성하고 주입해주어야 한다.

//    과거에는 수정자 주입과 필드 주입을 많이 사용했지만,
//    최근에는 스프링을 포함한 DI 프레임워크 대부분이 생성자 주입을 권장한다. 
//    그 이유는 다음과 같다 -> 불변, 누락


//    private final MemberRepository memberRepository;
//    private final DiscountPolicy discountPolicy; //즉 구체적인 것은 위와 같이 직접 정하지 말라는 말


    private MemberRepository memberRepository;
    private DiscountPolicy discountPolicy;
    @Autowired
    public void setMemberRepository(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    @Autowired
    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
        this.discountPolicy = discountPolicy;
    }

    //@Autowired //생성자가 하나밖에 없으면 생략 가능
    //이렇게 인터페이스가 뭐가 들어올지는 알지만 어떤 구현객체가 들어올지는 알지못함
    public OrderServiceImpl(MemoryMemberRepository memberRepository,
                            @MainDiscountPolicy DiscountPolicy discountPolicy) {
        // @Qualifier("mainDiscountPolicy") DiscountPolicy discountPolicy
        // 처럼 사용하여 해당 Qualifier와 일치하는 이름을 찾음
        // 생성자/수정자/필드 모두 사용가능
        // 만약 지정된 이름을 찾지 못하면??? >>> 스프링빈을 추가로 찾는다 >>> 그래도 없으면 예외오류
        // 하지만 @Qualifier는 @Qualifier를 찾는 용도로만 사용하는 것이 좋다(명확성)
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    //ctrl alt v
    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);

        return new Order(memberId, itemName, itemPrice, discountPrice);
    }


//    @Autowired
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }
//    @Autowired
//    public void setDiscountPolicy(DiscountPolicy discountPolicy) {
//        this.discountPolicy = discountPolicy;
//    }


    //테스트용
//    public MemberRepository getMemberRepository(){
//        return memberRepository;
//    }




}
