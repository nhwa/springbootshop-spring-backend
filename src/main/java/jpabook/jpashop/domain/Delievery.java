package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter
@Setter
public class Delievery {

    @Id
    @GeneratedValue
    @Column(name = "delievery_id")
    private Long id;

    @OneToOne(mappedBy = "delievery", fetch = LAZY)
    private Order order;

    @Embedded
    private Address address;


    @Enumerated(EnumType.STRING) // ORDINAL: 1,2 / STRING READY,COMP
    private DelieveryStatus status; // READY, COMP
}
