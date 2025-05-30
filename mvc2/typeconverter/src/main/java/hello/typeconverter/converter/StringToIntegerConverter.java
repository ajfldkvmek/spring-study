package hello.typeconverter.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StringToIntegerConverter implements Converter<String, Integer> {

    /*
    | 항목    | `parseInt()`                        | `valueOf()`                           |
    | ----- | ----------------------------          | ------------------------------------- |
    | 리턴 타입 | `int` (기본형)                       | `Integer` (객체형)                       |
    | 박싱 여부 | ❌ 직접 박싱 필요 (`new Integer` 등)      | ✅ 자동 박싱 또는 캐싱 사용                      |
    | 내부 구현 | `int` 리턴만 함                       | 내부적으로 `parseInt()` 호출 후 `Integer`로 박싱 |
    | 캐싱    | ❌ 없음                              | ✅ -128 \~ 127 범위 캐싱 (성능↑)             |

     */
    @Override
    public Integer convert(String source) {
        log.info("convert source: {}", source);
        return Integer.valueOf(source);
    }
}
