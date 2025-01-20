package hello.itemservice;

import hello.itemservice.web.validation.ItemValidator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ItemServiceApplication /*implements WebMvcConfigurer */ {

	public static void main(String[] args) {
		SpringApplication.run(ItemServiceApplication.class, args);
	}

	// SpringBootApplication 에 선언해주면 다른 컨트롤러에도 모두 적용된다
	// ValidationItemControllerV2 에 아래와 같이 Validator 를 선언하지 않아도 된다
	// 하지만 이렇게 글로벌 설정을 권장하지도 않고 잘 사용하지도 않는다
//	@InitBinder
//	public void initBinder(WebDataBinder dataBinder) {
//		dataBinder.addValidators(itemValidator);
//	}
//	public Validator getValidator() {
//		return new ItemValidator();
//	}

	/**
	 *  참고
	 *  검증시 `@Validated` `@Valid` 둘다 사용가능하다.
	 * `javax.validation.@Valid` 를 사용하려면`build.gradle` 의존관계 추가가 필요하다. - 자바 제공 Valid
	 * `implementation 'org.springframework.boot:spring-boot-starter-validation'`
	 * `@Validated` 는 스프링 전용 검증 애노테이션이고, `@Valid` 는 자바 표준 검증 애노테이션이다.
	 * 자세한 내용은 다음 Bean Validation에서 설명하겠다.
	 */
}
