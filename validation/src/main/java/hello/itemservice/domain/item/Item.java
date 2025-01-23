package hello.itemservice.domain.item;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

/**
 *   특정 필드(`FieldError` )가 아닌 해당 오브젝트 관련 오류(`ObjectError` )는 @ScriptAssert()를 사용하면 된다.
 *   하지만 @ScriptAssert() 의 경우 유지보수의 어려움 때문에 자바 15 버전부터 지원되지 않는다.
 *   해당 기능을 사용하고싶으면 maven 이나 gradle 에 의존을 추가해주면 된다.( GraalVM JavaScript 엔진 추가 )
 *
 *   maven
 *      <dependency>
 *          <groupId>org.graalvm.js</groupId>
 *          <artifactId>js</artifactId>
 *          <version>22.3.0</version>
 *      </dependency>
 *
 *   gradle
 *      implementation 'org.graalvm.js:js:22.3.0'
 *
 *  하지만 이렇게 하는 것은 제약사항이 많기 때문에 직접 검증규칙을 추가해주는 것을 권장한다.
 */
@Data
//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "10000원 넘게 입력해주세요")
public class Item {

    /**
     *  @NotBlank : 빈값 + 공백만 있는 경우를 허용하지 않는다.
     *  @NotNull` : `null` 을 허용하지 않는다.
     *  @Range(min = 1000, max = 1000000)` : 범위 안의 값이어야 한다.
     *  @Max(9999)` : 최대 9999까지만 허용한다.
     *
     *  참고**
     * `javax.validation.constraints.NotNull`
     *      -> `javax.validation` 으로 시작하면 특정 구현에 관계없이 제공되는 표준 인터페이스
     *
     * `org.hibernate.validator.constraints.Range`
     *      -> org.hibernate.validator 로 시작하면 하이버네이트 validator 구현체를 사용할 때만 제공되는 검증 기능이다.
     *          실무에서 대부분 하이버네이트 validator 를 사용하므로 자유롭게 사용하자(스프링에서도 기본적으로 제공됨)
     *
     *  @Valid 는 자바 표준 검증 어노테이션
     *  @Vdalidated 는 스프링제공 검증 어노테이션(내부에 groups 라는 기능이 있어 이 기능을 사용하려면 반드시 이놈을 사용해야함)
     *
     *  NotBlank 와 NotNull 의 차이는 공백값의 여부를 체크하지가의 차이이다.
     *  NotBlank    : 공백체크
     *  NotNull     : null 체크
     *
     *  문자열의 경우에는 NotBlank
     *  숫자의 경우 NotNull
     *  으로 사용하도록 하자
     *
     *  같은 객체를 바라보면서 다른 기능( 등룩, 수정 등 ) 을 하는 경우 같은 규칙을 사용할 때 충돌이 생긴다.
     *  이 때 해결 방법은 두 가지가 있다.
     *      1. groups 의 사용
     *      2. item 을 직접 쓰는 것이 아니라 saveForm, updateForm 같은 폼 전송을 위한 별도의 객체를 사용한다.
     *
     *  하지만 groups 를 사용하면 복잡성도 증가하고 단점도 많기 때문에 등록용 객체를 다르게 사용한다(실무에서는)
     */

//    @NotNull(groups = UpdateCheck.class)
    private Long id;

    //    @NotBlank(message = "공백x") //이 메세지는 기본값으로 설정한 값으로 나머지 검증규칙이 없을 때 마지막으로 적용된다
//    @NotBlank(groups = {UpdateCheck.class, SaveCheck.class})
    private String itemName;

//    @NotNull(groups = {UpdateCheck.class, SaveCheck.class})
//    @Range(min = 1000, max = 10000)
    private Integer price;

//    @NotNull(groups = {UpdateCheck.class, SaveCheck.class})
//    @Max(value = 9999, groups = {SaveCheck.class})
    private Integer quantity;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
