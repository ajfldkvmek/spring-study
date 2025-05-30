package hello.itemservice.web.basic;

import hello.itemservice.domain.item.Item;
import hello.itemservice.domain.item.ItemRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/basic/items")
@RequiredArgsConstructor
public class BasicItemController {

    private final ItemRepository itemRepository;

   // @Autowired
    //생성자가 하나면 어노테이션 생략 가능
//    public BasicItemController(ItemRepository itemRepository){
//        this.itemRepository = itemRepository;
//    }

//    @RequiredArgsConstructor 어노테이션 사용시
//    final 이 붙은 변수로 사용해 생성자를 자동생성해줌

    /**
     *  테스트용 초기화 데이터
     */
    @PostConstruct
    public void init(){
        itemRepository.save(new Item("itemA", 100000, 10));
        itemRepository.save(new Item("itemB", 200000, 20));
    }



    @GetMapping
    public String items(Model model){
        List<Item> items = itemRepository.findAll();
        model.addAttribute("items", items);

        return "basic/items";
    }



    @GetMapping("/{itemId}")
    public String item(@PathVariable("itemId") long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/item";
    }


    @GetMapping("/add")
    public String addForm(){
        return "basic/addForm";
    }


    //@PostMapping("/add")
    public String addItemV1(@RequestParam("itemName") String itemName,
                       @RequestParam("price") int price,
                       @RequestParam("quantity") Integer quantity,
                       Model model){

        Item item = new Item();
        item.setItemName(itemName);
        item.setPrice(price);
        item.setQuantity(quantity);

        itemRepository.save(item);

        model.addAttribute("item", item);

        return "basic/item";
    }


//    @PostMapping("/add")
    public String addItemV2(
            @ModelAttribute("item") Item item ){
        // @ModelAttribute("item") 를 사용하면
        // 알아서 객체에 값을 set 해줌
        itemRepository.save(item);
//        model.addAttribute("item", item)를 입력하지 않아도 (Model model 이라고 파라메터 설정할 필요도 없다)
//        model 에 알아서 다 입력된다. 저장되는 이름은 어노테이션에 설정해놓은 이름으로 저장됨 ( 이 메서드에서는 item )
        return "basic/item";
    }



//    @PostMapping("/add")
    public String addItemV3(
            @ModelAttribute Item item ){
        // @ModelAttribute("item") 를 사용하면
        // 알아서 객체에 값을 set 해줌
        itemRepository.save(item);
//        model.addAttribute("item", item)를 입력하지 않아도 (Model model 이라고 파라메터 설정할 필요도 없다)
//        model 에 알아서 다 입력된다. 저장되는 이름은 어노테이션에 설정해놓은 이름으로 저장됨 ( 이 메서드에서는 item )
//        만약 어노테이션에서 이름을 생략할 경우  Item -> item 과 같이 클래스명의 첫글자만 소문자로 바꾸어서 저장함
        return "basic/item";
    }



//    @PostMapping("/add")
    public String addItemV4(Item item ){
        // @ModelAttribute("item") 를 사용하면
        // 알아서 객체에 값을 set 해줌
        itemRepository.save(item);
//        model.addAttribute("item", item)를 입력하지 않아도 (Model model 이라고 파라메터 설정할 필요도 없다)
//        model 에 알아서 다 입력된다. 저장되는 이름은 어노테이션에 설정해놓은 이름으로 저장됨 ( 이 메서드에서는 item )
//        만약 어노테이션에서 이름을 생략할 경우  Item -> item 과 같이 클래스명의 첫글자만 소문자로 바꾸어서 저장함

//      심지어  @ModelAttribute 를 생략할 수도 있다!
//        이 방식이 우리가 흔히 사용하던 스프링에서의 뷰이동방법
        return "basic/item";
    }

//    @PostMapping("/add")
    public String addItemV5(Item item ){
        itemRepository.save(item);
        return "redirect:/basic/item/" + item.getId();
        //PRG 를 통해 데이터 입력요청시 새로고침을 통해 중복된 입력요청이 반복되는 것을 방지하기위함
        //하지만 이렇게 + string 을 통해 매핑 주소를 설정할 경우 공백이나 한글이 포함될 경우 에러가 발생함
        //이 때 문자 인코딩을 통해 매핑주소를 변경해야하는데 이 것을 처리하기 위한 메서드 RedirectAttribute 를 아라보자
    }



    @PostMapping("/add")
    public String addItemV6(Item item, RedirectAttributes redirectAttributes){
        Item savedItem = itemRepository.save(item);
        redirectAttributes.addAttribute("itemId", savedItem.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/basic/items/{itemId}";
        //이렇게 redirectAttributes 로 설정한 이름과 같으면 매핑주소 설정된다
        //redirectAttributes 로 설정한 attribute 가 더 많을 경우 나머지 값들은 쿼리스트링으로 넘어가게 된다.
        //또한 문자인코딩도 알아서 해결해준다
    }


    @GetMapping("/{itemId}/edit")
    public String editForm(@PathVariable("itemId") long itemId, Model model){
        Item item = itemRepository.findById(itemId);
        model.addAttribute("item", item);
        return "basic/editForm";
    }


    @PostMapping("/{itemId}/edit")
    public String edit(@PathVariable("itemId") long itemId, @ModelAttribute Item item){
        itemRepository.update(itemId, item);
        return "redirect:/basic/items/{itemId}";
    }




}
