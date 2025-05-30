package hello.login;

import hello.login.web.argumentresolver.LoginMemberArgumentResolver;
import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import jakarta.servlet.Filter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new LoginMemberArgumentResolver());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**") // 인터셉터는 하위 모두를 적용시킬때 /** 을 사용함 '/*' 는 경로 내에서
                .excludePathPatterns("/css/**", "/*.ico", "/error"); // 적용하지 않을 URL 패턴

        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**")
                .excludePathPatterns("/", "/members/add", "/login", "/logout", "/css/**", "/*.ico", "/error");
    }

    /**
     * 참고로
     * @ServletComponentScan 또는
     * @WebFilter(filterName = "logFilter", urlPatterns = "/*")
     * 두 가지 방법으로도 필터등록이 가능하지만
     * 필터의 순서조절이 되지않는다.
     *
     * 필터의 순서를 적용하려면
     * FilterRegistrationBean 를 적용하도록 하자
     */
//    @Bean
//    public FilterRegistrationBean logFilter() {
//            FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
//            filterFilterRegistrationBean.setFilter(new LogFilter()); // 생성한 필터 추가
//            filterFilterRegistrationBean.setOrder(1); // 해당 필터를 적용할 순서
//            filterFilterRegistrationBean.addUrlPatterns("/*"); // 필터를 적용하는 URL 패턴( 이 경우는 모든 URL 에 적용 )
//            return filterFilterRegistrationBean;
//    }


//    @Bean
//    public FilterRegistrationBean loginCheckFilter() {
//        FilterRegistrationBean<Filter> filterFilterRegistrationBean = new FilterRegistrationBean<>();
//        filterFilterRegistrationBean.setFilter(new LoginCheckFilter());
//        filterFilterRegistrationBean.setOrder(2);
//        filterFilterRegistrationBean.addUrlPatterns("/*");
//        return filterFilterRegistrationBean;
//    }

}
