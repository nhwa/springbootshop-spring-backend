package jpabook.jpashop.service;

import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional
    public void save(Item item){
        itemRepository.save(item);
    }

    /**
    * 1) 변경 감지 기능 사용( Dirty Checking )
     영속성 컨텍스트에서 엔티티를 다시 조회한 후에 데이터를 수정하는 방법
     트랜잭션 안에서 엔티티를 다시 조회, 변경할 값 선택 트랜잭션 커밋 시점에 변경 감지(Dirty Checking)가
     동작해서 데이터베이스에 UPDATE SQL 실행
     */
    @Transactional
    //리팩토링 : DTO 생성해서 많은 파라미터 관리
    public void updateItem(Long itemId,String name, int price, int stockQuantity, String isbn, String author){ //파라미터로 넘어온 준영속 상태의 엔티티

        Item item = itemRepository.findOne(itemId);
        item.setName(name);
        item.setPrice(price);
        item.setStockQuantity(stockQuantity);
        ((Book)item).setIsbn(isbn); // 리팩토링!
        ((Book)item).setAuthor(author); // 리팩토링!
        /**
         * 리팩토링 필요 => setter 쓰지말자.
         * findItem.change(name,price,stockQuantity...)
         * 의미있는 메서드를 만들어서 사용해야함 => 추적하기쉽게
         *
         * Book findItem = (Book) itemRepository.findOne(param.getId()); // 같은 엔티티를 조회
         * findItem.setPrice(param.getPrice()); // findItem으로 들어온 엔티티는 영속상태니 자동으로 관리해준다.
         * findItem.setName(param.getName());
         * findItem.setPrice(param.getPrice());
         * findItem.setStockQuantity(param.getStockQuantity());
         * findItem.setIsbn(param.getIsbn());
         * findItem.setAuthor(param.getAuthor());//데이터 수정
         * */

    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }
}
