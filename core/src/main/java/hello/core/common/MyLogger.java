package hello.core.common;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Scope(value = "request" , proxyMode = ScopedProxyMode.TARGET_CLASS)
//스코프를 request로 하면 http 요청당 하나씩 생성되고 요청 끝나면 사라짐
//Provider도 귀찮아??? 그럼 넌 proxyMode야
//요 스코프는 편리하지만 매우 주의해서 사용해야함(꼭 필요한 곳에서 최소화해서 사용)
public class MyLogger {

    private String uuid;
    private String requestURL;

    public void setRequestURL(String requestURL){
        this.requestURL = requestURL;
    }

    public void log(String message){
        System.out.println("[" + uuid + "]" +  "[" + requestURL + "] " + message );
    }

    @PostConstruct
    public void init() {
        uuid = UUID.randomUUID().toString();
        System.out.println("[" + uuid + "] request scope bean create: " + this);
    }

    @PreDestroy
    public void close(){
        System.out.println("[" + uuid + "] request scope bean close: " + this);
    }


}
