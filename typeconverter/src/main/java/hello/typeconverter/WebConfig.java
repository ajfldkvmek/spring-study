package hello.typeconverter;

import hello.typeconverter.converter.IntegerToStringConverter;
import hello.typeconverter.converter.IpPortToStringConverter;
import hello.typeconverter.converter.StringToIntegerConverter;
import hello.typeconverter.converter.StringToIpPortConverter;
import hello.typeconverter.formatter.MyNumberFormatter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     *
     * @param registry
     *  스프링은 기본적으로 내부에서 ConversionService 를 공한다,
     *  스프링에서 추가적인 컨버터를 적용하기 위해서는 추가로 설정이 필요하다 -> WebMvcConfigurer
     *  이 경우 개인이 추가한 컨버터가 우선적으로 적용된다.
     *
     *  HttpMessageConverter 에는 컨버젼서비스가 적용되지 않으니 주의하자
     *  즉 JSON 으로 데이터가 변환되는 것은 jackson 같은 라이브러리에 달린것이디 때문에
     *  이놈이 처리하기 나름이다 이말이야 -> 라이브러리에서 포매팅 방식을 찾아라
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
//        registry.addConverter(new StringToIntegerConverter());
        registry.addConverter(new StringToIpPortConverter());
//        registry.addConverter(new IntegerToStringConverter());
        registry.addConverter(new IpPortToStringConverter());

        registry.addFormatter(new MyNumberFormatter());
    }
}
