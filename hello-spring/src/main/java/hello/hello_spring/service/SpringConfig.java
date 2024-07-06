package hello.hello_spring.service;

import hello.hello_spring.aop.TimeTraceAOP;
import hello.hello_spring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *  자바코드로 직접 스프링 빈 등록하기 (과거에는 xml로 설정하였음)
 */
@Configuration
public class SpringConfig {

//    private DataSource dataSource;
//    @Autowired
//    public SpringConfig(DataSource dataSource){
//        this.dataSource = dataSource;
//    }

//    @PersistenceContext
//    private EntityManager em;
//
//    @Autowired
//    public SpringConfig(EntityManager em){
//        this.em = em;
//    }

    private final MemberRepository memberRepository;

    @Autowired
    public SpringConfig(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }


//    @Autowired
//    public SpringConfig(MemberRepository memberRepository){
//        this.memberRepository = memberRepository;
//    }

    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository);
    }

//    @Bean
//    public MemberRepository memberRepository(){
//        return new JdbcMemberRepository(dataSource);
//        return new JpaMemberRepository(em);
//    }


//    @Bean
//    public TimeTraceAOP timeTraceAOP() {
//        return new TimeTraceAOP();
//    }

}
