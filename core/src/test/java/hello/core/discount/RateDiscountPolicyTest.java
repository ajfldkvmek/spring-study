package hello.core.discount;

import hello.core.member.Grade;
import hello.core.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class RateDiscountPolicyTest {

    RateDiscountPolicy rateDiscountPolicy = new RateDiscountPolicy();

    @Test
    @DisplayName("VIP가 아니면 할인이 적용되지 않아야 한다.")
    void vip_o() {
        Member member = new Member(1L, "memberVIP", Grade.VIP);

        //ctrl + alt + v
        int discount = rateDiscountPolicy.discount(member, 10000);

        //Assertions 같은 경우는 static으로 import 해주는 것이 좋다
        assertThat(discount).isEqualTo(1000);

    }


    @Test
    @DisplayName("VIP는 10%할인이 적용되어야 한다.")
    void vip_x() {
        Member member = new Member(1L, "memberBASIC", Grade.BASIC);

        //ctrl + alt + v
        int discount = rateDiscountPolicy.discount(member, 10000);

        assertThat(discount).isEqualTo(0);

    }



}