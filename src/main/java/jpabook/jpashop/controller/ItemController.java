package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/items/new")
    public String createForm(Model model){
        model.addAttribute("form", new BookForm());

        return "items/createItemForm";
    }

    @PostMapping(value = "/items/new")
    public String create(BookForm form){
        Book book = new Book();
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setIsbn(form.getIsbn());
        book.setAuthor(form.getAuthor());

        itemService.save(book);

        return "redirect:/items";
    }
    @GetMapping(value = "items")
    public String list(Model model){
        //API 만들때는 절대 entity를 반환하지 말아야한다!!! 외부로 노출하지마시오.
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);

        return "items/itemList";
    }

    @GetMapping(value = "items/{itemId}/edit") // {A} => @PathVariable("A")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model ){
        Book item = (Book) itemService.findOne(itemId);
        //화면에 기존값 보여주기
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());

        model.addAttribute("form", form);
        return "items/updateItemForm";
    }

    // 리팩토링할때 보안상 접근권한으로 유저가 바꿀수 없게 조절해야한다.
    @PostMapping(value = "items/{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form){

        /** 컨트롤러에서 엔티티 생성하지 말고 서비스 계층에 식별자와 변경할 데이터를 명확하게 전달하는게 필요
         * 엔티티는 서비스계층에서 직접 변경하는게 권장
         * Book book = new Book();
         * book.setId(form.getId());
         * book.setName(form.getName());
         * book.setPrice(form.getPrice());
         * book.setStockQuantity(form.getStockQuantity());
         * book.setAuthor(form.getAuthor());
         * book.setIsbn(form.getIsbn());
         *
         *  itemService.save(book);
        */

        itemService.updateItem(itemId, form.getName(), form.getPrice(), form.getStockQuantity(), form.getIsbn(), form.getAuthor());

        return "redirect:/items";
    }

}
