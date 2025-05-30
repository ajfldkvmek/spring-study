package hello.itemservice.web.validation;


import hello.itemservice.domain.item.Item;
import hello.itemservice.web.validation.form.ItemSaveForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/validation/api/items")
public class ValidationItemApiController {

    @PostMapping("/add")
    public Object addItem(@RequestBody @Validated ItemSaveForm form, BindingResult bindingResult) {

        /**
         *  RequestBody 로 받을 때 에러가 발생한 경우
         *
         *  필드 단위로 세밀하게 처리되어 특정 필드가 바인딩 되지 않아도 나머지 필드들은 정상적으로 검증되는 ModelAttribute 와는 다르게
         *
         *  RequestBody 는 JSON 형태로 전달되는 특성상 Convert 단계에서 실패하면 다음단계로 진행되지 않고 예외가 발생한다.
         */
        log.info("API 컨트롤러 호출");

        if (bindingResult.hasErrors()) {
            log.info("검증 오류 발생 errors: {}", bindingResult);

            return bindingResult.getAllErrors();
        }

        log.info("성공로직 실행");

        return form;
    }
}
