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

    @PostMapping("/add")
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

}

