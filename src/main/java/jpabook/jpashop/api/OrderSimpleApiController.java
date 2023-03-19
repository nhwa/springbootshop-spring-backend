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

    /**
     * V2. 엔티티를 조회해서 DTO로 변환(fetch join 사용X)
     * - 단점: 지연로딩으로 쿼리 N번 호출
     */
    @GetMapping("api/v2/simple-orders")
    public List<SimpleOrderDto> ordersV2(){
        // ORDER 2개인 경우
        //N + 1 -> 1 + 회원2 + 배송 2 = 5번 쿼리 (같은 회원인경우 -> 1 + 회원1 + 배송 2 = 4번)
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o)).collect(Collectors.toList());
        return result;
    }
    @Data
    static class SimpleOrderDto {
        private Long orderID;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order){
            orderID = order.getId();
            name = order.getMember().getName(); //LAZY 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //LAZY 초기화
        }


    }
}
