package hello.core.singleton;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

public class StatefulServiceTest {

    @Test
    void statefulServiceSingletest(){
        ApplicationContext ac = new AnnotationConfigApplicationContext(TestConfig.class);

        StatefulService statefulService1 = ac.getBean(StatefulService.class);
        StatefulService statefulService2 = ac.getBean(StatefulService.class);

        //threadA: A사용자가 10000원 주문
        int userAprice = statefulService1.order("userA", 10000);
        //threadB: B사용자가 20000원 주문
        int userBprice = statefulService2.order("userB", 20000);


        //threadA: A사용자가 주문금액 조회
//        int price = statefulService1.getPrice();
        System.out.println("price = " + userAprice);
        //threadB: B사용자가 주문금액 조회
    }

    static class TestConfig {

        @Bean
        public StatefulService statefulService() {
            return new StatefulService();
        }
    }
}
