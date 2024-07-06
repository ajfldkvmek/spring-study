package hello.core.scope;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Provider;
import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.ObjectFactory;
//import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class SingletonWithPrototypeTest1 {

    //스프링은 기본적으로 싱글톤 빈을 사용한다
    //하지만 우리는 프로토타입빈을 사용하고싶다
    //하지만 이렇게 싱글톤과 프로토타입빈을 같이 사용할 경우 문제가 발생하게 된다

    @Test
    void prototypeFind(){

        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);

        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        assertThat(prototypeBean2.getCount()).isEqualTo(1);

    }

    @Test
    void singletonClientUsePrototype(){

        AnnotationConfigApplicationContext ac =
                new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);


        ClientBean clientBean1 = ac.getBean(ClientBean.class);
        int count1 = clientBean1.logic();
        assertThat(count1).isEqualTo(1);

        ClientBean clientBean2 = ac.getBean(ClientBean.class);
        int count2 = clientBean2.logic();
        assertThat(count2).isEqualTo(1);
        
        //이렇게 싱글톤내에서 프로토타입빈을 불러올 경우
        //이미 만들어진 프로토타입빈이므로 생성시점에 이미 주입된 상태임
        //즉 새로 불러올 때 마다 만들어지는 것이 아니라
        //이미 주입된 놈을 불러오는 것

        //그럼 로직을 호출할 때 마다 새로 만들려면???
        //1.그냥 호출할때마다 불러오기
        //2.JSR-330(자바표준) -> java.inject:javax.inject:1 라이브러리를 gradle에 추가해야함
        //(스프링부트 3.0이상의 경우 jakarta.inject:jakarta.inject-api:2.0.1 추가)
        
    }

    @Scope("singleton")
    static class ClientBean {

        @Autowired
        private Provider<PrototypeBean> provider;

//        private ObjectProvider<PrototypeBean> prototypeBeanProvider; // ObjectFactory + 몇가지 기능
        //private ObjectFactory<PrototypeBean> prototypeBeanObjectFactory;

        public int logic() {
//            PrototypeBean prototypeBeans = prototypeBeanProvider.getObject();
            PrototypeBean prototypeBean = provider.get();
            prototypeBean.addCount();
            return prototypeBean.getCount();
        }
    }


//    만약 아래와 같이 할 경우 호출 시 새로 생기긴 하지만 그거도 한계가 있따
//    @Scope("singleton")
//    static class ClientBean2 {
//
//        private final PrototypeBean prototypeBean2; 생성시점에 주입
//
//        public ClientBean2(PrototypeBean prototypeBean){
//            this.prototypeBean = prototypeBean;
//        }
//
//        public int logic() {
//            prototypeBean.addCount();
//            return prototypeBean.getCount();
//        }
//    }


    @Scope("prototype")
    static class PrototypeBean {

        private int count = 0;

        public void addCount(){
            count++;
        }

        public int getCount() {
            return count;
        }


        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init" + this);
        }

        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy"); //호출안되지만 그냥 씀 ㅎ
        }

    }

}
