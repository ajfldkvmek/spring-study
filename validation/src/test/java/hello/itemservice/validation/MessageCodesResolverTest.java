package hello.itemservice.validation;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BindingResult;
import org.springframework.validation.DefaultMessageCodesResolver;
import org.springframework.validation.FieldError;
import org.springframework.validation.MessageCodesResolver;

import static org.assertj.core.api.Assertions.*;

public class MessageCodesResolverTest {

    MessageCodesResolver codesResolverTest = new DefaultMessageCodesResolver();

    @Test
    void messageCodesResolverObject() {
        String[] messageCodes = codesResolverTest.resolveMessageCodes("required", "item");
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
        assertThat(messageCodes).containsExactly("required.item", "required");
    }


    @Test
    void messageCodesResolverField() {
        String[] messageCodes = codesResolverTest.resolveMessageCodes("required", "item", "itemName", String.class);
        for (String messageCode : messageCodes) {
            System.out.println("messageCode = " + messageCode);
        }
//        BindingResult.rejectValue() 가 이 MessageResolver 를 사용한다
//        new FieldError("item", "itemName", null, false, messageCodes, null, null, ...);
        assertThat(messageCodes).containsExactly(
                "required.item.itemName",
                 "required.itemName",
                 "required.java.lang.String",
                 "required"
        );
    }
}
