package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {

    @Test
    public void lifeCycleTest(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client = ac.getBean(NetworkClient.class);
        ac.close(); // 그냥 ApplicationContext 로하면 close() 못씀
    }

    @Configuration
    static class LifeCycleConfig {

        //이렇게 되면 메서드명을 자유롭게 줄 수 있다
        //스프링빈이 스프링코드에 의존하지 않는다
        //코드가 아니라 설정정보를 사용하기 때문에 코드를 고칠 수 업슨 외부 라이브러리에도 메소드 사용가능 ###이게 제일 중요
        
        //destroyMethod의 경우 default설정이 (추론)으로 되어있어
        //close, shutdown이 있는 경우 해당 메소드를 가져와서 실행함
        //공백으로 설정할 경우 추론 기능 동작x
        
//        @Bean(initMethod="init", destroyMethod="close")
        @Bean
        public NetworkClient networkClient() {
            NetworkClient networkClient = new NetworkClient();
            networkClient.setUrl("https://www.naver.com");
            return networkClient;
        }
    }

}
