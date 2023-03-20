package jpabook.jpashop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "Delivery_id")
    private Long id;

    @JsonIgnore
    @OneToOne(mappedBy = "delivery", fetch = LAZY) //무조건
    private Order order;

    @Embedded
    private Address address;


    @Enumerated(EnumType.STRING) // ORDINAL: 1,2 / STRING READY,COMP
    private DeliveryStatus status; // READY, COMP
}
