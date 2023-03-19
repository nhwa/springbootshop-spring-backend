package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * xToOne(ManyToOne, OneToOne) 관계 최적화
 * Order
 * Order -> Member
 * Order -> Delivery
 *
 */

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    /**
     * V1. 엔티티 직접 노출
     *
     *  양방향 관계 문제 발생 -> @JsonIgnore
     *
     *  order - member 와 order - address 는 지연 로딩(양방향 연관관계를 계속 로딩)이다. 따라서 실제엔티티 대신에 프록시 존재
     *  jackson 라이브러리는 기본적으로 이 프록시객체를 json으로 어떻게 생성해야하는지 모름 예외발생
     *  => Hibernate5Module을 스프링 빈으로등록하면해결(스프링 부트사용중) Hibernate5Module 모듈 등록, LAZY=null 처리
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1(){
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기화
            //orderItem은 lazy 강제 초기화 안했기때문에 노출 x
        }
        return all;
    }

}
