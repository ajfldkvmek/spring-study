package hello.hello_spring.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;

@Aspect
@Component //로 컴포넌트 스캔을 하거나 빈 등록을 통해서 함
public class TimeTraceAOP {

    @Around("execution(* hello.hello_spring..*(..))")
    public Object excute(ProceedingJoinPoint joingPoint) throws Throwable {

        long start = System.currentTimeMillis();
        System.out.println("Start: " + joingPoint.toString());
        try {
            return joingPoint.proceed();
        } finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("END: " + joingPoint.toString() + " " + timeMs + "ms");
        }


    }
}
