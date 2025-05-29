package hello.typeconverter.formatter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.format.Formatter;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
 *  기본적으로 String 은 알아서 처리하기 때문에 이외의 타입들만 처리하면된다.
 */

@Slf4j
public class MyNumberFormatter implements Formatter<Number> {

    @Override
    public Number parse(String text, Locale locale) throws ParseException {
        log.info("text : {}, locale: {}", text, locale);
        // "1,000" -> 1000
        // 나라마다 다른 숫자포맷을 적용해서 지역에 맞게 포맷해줌
        NumberFormat format = NumberFormat.getInstance(locale);
        return format.parse(text);
    }

    @Override
    public String print(Number object, Locale locale) {
        log.info("object : {}, locale: {}", object, locale);
        return NumberFormat.getInstance(locale).format(object);
    }
}
