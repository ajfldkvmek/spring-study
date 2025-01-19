package hello.itemservice.web.validation;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/validation/v1/items")
@RequiredArgsConstructor
public class ValidationItemControllerV1 {

    private final ItemRepository itemRepository;

    @GetMapping
    public String items(Model model) {
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);
        return "validation/v1/items";
    }

    @GetMapping("/{itemId}")
    public String item(@PathVariable long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/item";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("item", new Item());
        return "validation/v1/addForm";
    }

    @PostMapping("/add")
    public String addItem(@ModelAttribute Item item, RedirectAttributes redirectAttributes, Model model) {

        //검증 오류 보관
        Map<String, Object> errors = new HashMap<>();

        //검증 로직
        if (!StringUtils.hasText(item.getItemName())) {
            errors.put("itemName", "Item name is required");
        }
        if(item.getPrice() == null || item.getPrice() < 1000 || item.getPrice() > 10000) {
            errors.put("price", "Price must be between 1000 and 10000");
        }
        if(item.getQuantity() == null || item.getQuantity() >= 9999) {
            errors.put("quantity", "Quantity must be between 0 and 9999");
        }

        //복합 룰 검증
        if(item.getPrice() != null && item.getQuantity() != null) {
            //for Mac: opt + command + V
            int resultPrice = item.getPrice() * item.getQuantity();
            if (resultPrice < 10000) {
                errors.put("globalError", "Price * Quantity must be greater than 10000");
            }
        }

        //검증 실패하면 다시 입력  폼으로
        if(!errors.isEmpty()) {
            log.info("errors: {}", errors);
            model.addAttribute("errors", errors);
            return "validation/v1/addForm";
        }

        //성공 로직
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/validation/v1/items/{itemId}";
    }

    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable Long itemId, Model model) {
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "validation/v1/editForm";
    }

    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable Long itemId, @ModelAttribute Item item) {
        itemRepository.update(itemId, item);
        return "redirect:/validation/v1/items/{itemId}";
    }

}

