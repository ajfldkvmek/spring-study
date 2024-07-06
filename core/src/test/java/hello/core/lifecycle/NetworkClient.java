package hello.core.lifecycle;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.stream.DoubleStream;

public class NetworkClient  {

    //implements InitializingBean, DisposableBean
    //InitializingBean, DisposableBean 의 단점
    //스프링 전용 인터페이스임
    //그리고 요즘은 거의 사용하지 않음

    private String url;

    public NetworkClient() {
        System.out.println("생성자 호출, url = " + url);
    }

    public void setUrl(String url){
        this.url = url;
    }

    public void connect() {
        System.out.println("connect: " + url);

    }

    public void call(String message) {
        System.out.println("call: "+ url + " message = " + message );
    }

    public void disconnect() {
        System.out.println("close: " + url);
    }


//    @Override
//    public void afterPropertiesSet() throws Exception {
//        //의존관계 주입이 끝나면 호출하겠다는 말
//        System.out.println("NerworkClient.afterPropertiesSet");
//        connect();
//        call("초기화 연결 메세지");
//    }
//
//
//    @Override
//    public void destroy() throws Exception {
//        System.out.println("NerworkClient.destroy");
//        disconnect();
//    }

    //이런식으로 어노테이션으로 실행시점 설정해주는 것이 좋다(이거 제일 많이 씀) - 스프링 권장사항
    //이는 스프링 종속적인 기술이 아니기 때문에 다른 컨테이어에서도 잘 동작한다
    //단점으로는 외부 라이브러리에는 사용하지 못한다는 점...(이 경우는 앞서 사용한 @Bean() 을 사용하자
    @PostConstruct
    public void init() {
        System.out.println("NetworkClient.init");
        connect();
        call("초기화 연결 메세지");
    }

    @PreDestroy
    public void close(){
        System.out.println("NetworkClient.close");
        disconnect();
    }

}
