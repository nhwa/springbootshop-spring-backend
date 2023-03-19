package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id //primary Key
    @GeneratedValue //db에 자동생성
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded //컬럼을 하나의 객체로 사용하고 싶을때 사용!
    private Address address;

    /**
     * Member < Order(주인)
     * 연관관계 주인이 아닌 곳에 ( mappedby = )
     * 오더 테이블에있는 멤버 필드에 의해서 맵핑된다.
     */
    @JsonIgnore
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    //DB에서 사용하는 네이밍은 snakecase
    @Column(name ="created_at")
    //JAVA같은에서 사용하는 네이밍은 camelcase
    private LocalDateTime createdAt;

    @Column(name ="updated_at")
    private LocalDateTime updatedAt;
}
