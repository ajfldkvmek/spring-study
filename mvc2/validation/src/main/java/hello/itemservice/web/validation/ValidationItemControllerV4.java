package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import hello.itemservice.domain.item.SaveCheck;
import hello.itemservice.domain.item.UpdateCheck;
import hello.itemservice.web.validation.form.ItemSaveForm;
import hello.itemservice.web.validation.form.ItemUpdateForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/validation/v4/items")
@RequiredArgsConstructor
public class ValidationItemControllerV4 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v4/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v4/addForm";
    }

    @PostMapping("/add")
    public String addItem(@Validated @ModelAttribute("item") ItemSaveForm form, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {
        /**
         *  검증 순서
         * 1. `@ModelAttribute` 각각의 필드에 타입 변환 시도
         *      1. 성공하면 다음으로
         *      2. 실패하면 `typeMismatch' 로 `FieldError` 추가
         *
         * 2. Validator 적용
         *
         * 이다.
         *
         * 우선 ModelAttribute 를 통해 바인딩을 먼저 실행하고
         * 바인딩에 성공한 필드들에 대해서만 검증규칙이 적용된다
         *
         * 만약  @ModelAttribute("item") 에서 item 을 생략하게 되면 form 에서 전달되는 변수명이 객체의 이름으로 설정됨
         */
//        이렇게 @Validated 어노테이션을 추가해주면
//        spring-boot-starter-validation` 라이브러리를 넣으면 자동으로 Bean Validator 를 인지하고 스프링에 통합된다
//        (LocalValidatorFactoryBean` 을 글로벌 Validator 로 등록
//        itemValidator.validate(item, bindingResult);

        //특정 필드 예외가 아닌 전체 예외 - 오브젝트 오류의 경우 직접 규칙을 설정하는 것이 좋다
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

//        검증 실패하면 다시 입력  폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors: {}", bindingResult);
            return "validation/v4/addForm";
        }


        //성공 로직
        Item item = new Item();
        item.setItemName(form.getItemName());
        item.setPrice(form.getPrice());
        item.setQuantity(form.getQuantity());

        Item savedItem = itemRepository.save(item);

        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v4/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v4/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @Validated @ModelAttribute("item") ItemUpdateForm form, BindingResult bindingResult) {
        //특정 필드 예외가 아닌 전체 예외 - 오브젝트 오류의 경우 직접 규칙을 설정하는 것이 좋다
        if (form.getPrice() != null && form.getQuantity() != null) {
            int resultPrice = form.getPrice() * form.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000,
                        resultPrice}, null);
            }
        }

//        검증 실패하면 다시 입력  폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors: {}", bindingResult);
            return "validation/v4/editForm";
        }


        Item itemParam = new Item();
        itemParam.setItemName(form.getItemName());
        itemParam.setPrice(form.getPrice());
        itemParam.setQuantity(form.getQuantity());
        itemRepository.update(itemId, itemParam);
        return "redirect:/validation/v4/items/{itemId}";
    }

}

