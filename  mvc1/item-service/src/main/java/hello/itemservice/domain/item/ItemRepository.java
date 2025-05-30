package hello.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//테스트 케이스 생성
//ctrl + shift + T
@Repository
public class ItemRepository {


    // 간단한 프로그램을 구성할 경우네는 Map 이나 Long 을 static 로 선언하여 작성해면 되지만
    // 실제로 운영을 하기 위해서는 동시성제어를 위해
    // concurrentMap 이나 AtomicLing 을 사용해야함
    private static final Map<Long, Item> store = new HashMap<>();
    private static long sequence = 0L;

    public Item save(Item item){
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }


    public Item findById(Long id){
        return store.get(id);
    }


    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    
    // 중복이냐 명확성이냐 의 문에제서는 명확성이 더 중요하다
    //사실 이 경우에는 파라메터만 있는 객체를 새로 만들어서
    //이를 통해서 데이터를 제어하는 것이 더 좋다
    public void update(Long itemId, Item updateParam) {
        Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());

    }

    public void clearStore(){
        store.clear();
    }


}
