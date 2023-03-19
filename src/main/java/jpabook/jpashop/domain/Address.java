package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;


/**
 * JPA 내장 타입 생략가능!
 * 값타입 : setter를 아예 제공하지말고 생성자에서 값을 모두 초기화하는 변경 불가능한 클래스로!
 */
@Embeddable
@Getter
public class Address {
    private String city;
    private String street;
    private String zipcode;

    protected Address(){}// 함부로 NEW로 생성못하게 -> JPA스펙에 맞게

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
