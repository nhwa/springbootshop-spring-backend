package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
@Table(name="orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // Order (주인) > Member JoinColumn
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member ;

    //  Order < OrderItem (주인)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL) // Order(연관주) > Delievery JoinColumn
    @JoinColumn(name = "delievery_id")
    private Delievery delievery;

    private LocalDateTime orderDate;

    private OrderStatus status; //주문상태 [ORDER,CANCEL]

    //==연관관계 (편의) 메서드==// 양뱡향일때 핵심적으로 컨트롤하는 엔티티쪽이 갖고있는게 좋음
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelievery(Delievery delievery){
        this.delievery = delievery;
        delievery.setOrder(this);
    }

    //==생성 메서드==//
    public static Order createOrder(Member member, Delievery delievery, OrderItem... orderItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelievery(delievery);
        for (OrderItem orderItem : orderItems) {
         order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());

        return order;
    }

    //==비즈니스 로직==//
    /** 주문 취소 */
    public void cancel(){
        if(delievery.getStatus() == DelieveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    //==조회 로직==//
    /** 전체 주문 가격 조회 */
    public int getTotalPrice(){
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }
}
