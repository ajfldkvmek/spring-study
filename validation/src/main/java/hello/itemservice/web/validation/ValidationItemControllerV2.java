package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v2/items")
@RequiredArgsConstructor
public class ValidationItemControllerV2 {

    private final ItemRepository itemRepository;
    private final ItemValidator itemValidator;

    /**
     *
     * @param dataBinder
     * 해당 컨트롤러가 호출될 때 항상 먼저 이 메소드가 호출되며 검증규칙이 적용된다(해당 컨트롤러에서만 적용됨)
     */
//    @InitBinder
//    public void initBinder(WebDataBinder dataBinder) {
//        dataBinder.addValidators(itemValidator);
//    }
    //생성자가 하나일 때는 자동으로 의존성 주입이 된다
//    public ValidationItemControllerV2(ItemRepository itemRepository, ItemValidator itemValidator) {
//        this.itemRepository = itemRepository;
//        this.itemValidator = itemValidator;
//    }

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v2/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v2/addForm";
    }

//    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        /**
         *  BindingResult 는 스프링에서 제공되는 객체
         *  중요한 점은 BindingResult 는 반드시 model 객체 뒤로 와야한다!
         */
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            //model 오브젝트명, field 명, message 순서로 입력
            bindingResult.addError(new FieldError("item", "itemName", "Item name is required"));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 10000) {
            bindingResult.addError(new FieldError("item", "price", "Price must be between 1 and 10000"));
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity", "Quantity must be between 1 and 9999"));
        }

        //복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            //for Mac: opt + command + V
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", "Price must be between 1 and 10000"));
            }
        }

        //검증 실패하면 다시 입력  폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors: {}", bindingResult);
//            model.addAttribute("errors", bindingResult);
            //  bindingResult 는 자동으로 model 객체에 담겨 전달되기 때문에 굳이 추가작업을 해줄 필요는 없다
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v2/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV2(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        /**
         *  BindingResult 는 스프링에서 제공되는 객체
         *  중요한 점은 BindingResult 는 반드시 model 객체 뒤로 와야한다!
         *
         *  일반적으로 ModelAttribute에 바인딩 시 타입 오류가 발생하면?
         *  400 오류가 발생하면서 컨트롤러가 호출되지 않고, 오류 페이지로 이동한다
         *  하지만 `BindingResult` 가 있는 경우 오류 정보를 담아서 컨트롤러를 정상호출해준다
         *  BindingResult 에서 오류를 검증하는 방법으로는
         *      1. 스프링 자체적으로 FieldError 를 생성해서 bindingResult 에 담아준다.
         *      2. 개발자가 직접 넣는다.
         *      3. Validator 사용
         *
         *  FieldError는 두가지 생성자를 제공한다(하단 코드 참조)
         *
         *  paramlist
         *      objectName` : 오류가 발생한 객체 이름
         *      field` : 오류 필드
         *      rejectedValue` : 사용자가 입력한 값(거절된 값)
         *      bindingFailure` : 타입 오류 같은 바인딩 실패인지, 검증 실패인지 구분 값
         *      codes` : 메시지 코드
         *      arguments` : 메시지에서 사용하는 인자
         *      defaultMessage` : 기본 오류 메시지
         *      타임리프의 `th:field` 는 매우 똑똑하게 동작하는데, 정상 상황에는 모델 객체의 값을 사용하지만, 오류가 발생하면
         *      FieldError` 에서 보관한 값을 사용해서 값을 출력한다
         */
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            //model 오브젝트명, field 명, message 순서로 입력
//            bindingResult.addError(new FieldError("item", "itemName", "Item name is required"));
            bindingResult.addError(new FieldError("item", "itemName",
                                                    item.getItemName(), false, null, null,
                                                    "Item name is required"));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 10000) {
//            bindingResult.addError(new FieldError("item", "price", "Price must be between 1 and 10000"));
            bindingResult.addError(new FieldError("item", "price",
                                                    item.getPrice(), false, null, null,
                                        "Price must be between 1000 and 10000"));
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
//            bindingResult.addError(new FieldError("item", "quantity", "Quantity must be between 1 and 9999"));
            bindingResult.addError(new FieldError("item", "quantity",
                                                    item.getQuantity(), false, null, null,
                                        "Quantity must be between 1 and 9999"));
        }

        //복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            //for Mac: opt + command + V
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item",
                                                        null, null,
                                            "Price must be between 1 and 10000"));
            }
        }

        //검증 실패하면 다시 입력  폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors: {}", bindingResult);
//            model.addAttribute("errors", bindingResult);
            //  bindingResult 는 자동으로 model 객체에 담겨 전달되기 때문에 굳이 추가작업을 해줄 필요는 없다
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }
//    @PostMapping("/add")
    public String addItemV3(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        /**

         */
        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.addError(new FieldError("item", "itemName",
                    item.getItemName(), false, new String[]{"required.item.itemName"}, null, null ));
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.addError(new FieldError("item", "price",
                    item.getPrice(), false, new String[]{"range.item.price"}, new Object[]{1000, 1000000}, null));
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.addError(new FieldError("item", "quantity",
                    item.getQuantity(), false, new String[]{"max.item.quantity"}, new Object[]{10000}, null));
        }

        //복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            //for Mac: opt + command + V
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.addError(new ObjectError("item", new String[]{"totalPriceMin"}, new Object[]{10000, resultPrice}, null));
            }
        }

        //검증 실패하면 다시 입력  폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors: {}", bindingResult);
//            model.addAttribute("errors", bindingResult);
            //  bindingResult 는 자동으로 model 객체에 담겨 전달되기 때문에 굳이 추가작업을 해줄 필요는 없다
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

//    @PostMapping("/add")
    public String addItemV4(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

//        이렇게 스프링에서 제공하는 ValidationUtils 를 사용하여 간단하게 표현할 수도 있다.
//        복잡한 조건은 제공되지는 않고 간단한 기능만 제공된다
//        아래 코드는 서로 동일한 기능을 한다.
//        ValidationUtils.rejectIfEmptyOrWhitespace(bindingResult, "itemName", "required");

        //이렇게 bindingResult 의 에러체크를 해주면 스프링에서 제공되는 오류메세지만 담아서 바로 return 이 가능하다
        if(bindingResult.hasErrors()) {
            log.info("errors: {}", bindingResult);
            return "validation/v2/addForm";
        }

        if (!StringUtils.hasText(item.getItemName())) {
            bindingResult.rejectValue("itemName", "required");
        }

        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 1000000) {
            bindingResult.rejectValue("price", "range", new Object[]{1000, 1000000}, null );
        }

        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
            bindingResult.rejectValue("quantity", "max", new Object[]{9999}, null);
        }

        //복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                bindingResult.reject("totalPriceMin", new Object[]{10000, resultPrice}, null);
            }
        }

        //검증 실패하면 다시 입력  폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors: {}", bindingResult);
//            model.addAttribute("errors", bindingResult);
            //  bindingResult 는 자동으로 model 객체에 담겨 전달되기 때문에 굳이 추가작업을 해줄 필요는 없다
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


//    @PostMapping("/add")
    public String addItemV5(@ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

        itemValidator.validate(item, bindingResult);

        //검증 실패하면 다시 입력  폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors: {}", bindingResult);
//            model.addAttribute("errors", bindingResult);
            //  bindingResult 는 자동으로 model 객체에 담겨 전달되기 때문에 굳이 추가작업을 해줄 필요는 없다
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }

    /**
     *
      * @param item
     * @param bindingResult
     * @param redirectAttributes
     * @param model
     * @return
     *
     * @Validated 어노테이션을 사용하면
     * 검증규칙을 따로 선언하지 않아도 알아서 bindingResult 에 검증 결과를 담아준다
     *
     */
    @PostMapping("/add")
    public String addItemV6(@Validated @ModelAttribute Item item, BindingResult bindingResult, RedirectAttributes redirectAttributes, Model model) {

//        itemValidator.validate(item, bindingResult);
        //검증 실패하면 다시 입력  폼으로
        if(bindingResult.hasErrors()) {
            log.info("errors: {}", bindingResult);
//            model.addAttribute("errors", bindingResult);
            //  bindingResult 는 자동으로 model 객체에 담겨 전달되기 때문에 굳이 추가작업을 해줄 필요는 없다
            return "validation/v2/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v2/items/{itemId}";
    }


}

