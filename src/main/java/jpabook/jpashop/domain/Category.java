package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name="category_id")
    private Long id;

    private String name;

    /**
     * 실무에서는 ManyToMany 사용xxx
     *  CategoryItem 엔티티를 만들고 ManyToOne OneToMany로 매핑해서 사용하는게 좋음
     *  item 다대다 category ->  item 일대다 category_item 다대일 category
     */
    //
    @ManyToMany
    @JoinTable(name = "category_item",
        joinColumns = @JoinColumn(name="category_id"),
        inverseJoinColumns = @JoinColumn(name = "item_id"))
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = LAZY)     // 셀프로 양방향 연관관계 매핑
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>(); // 컬렉션은 필드에서 바로 초기화
    /** WHY 엔티티를 영속화할때 하이버네이트가 제공하는 내장 컬렉션으로 변경해버리니
     */


    //==연관관계 (편의) 메서드==//
    //parent child 양방향
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }

}
