package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {} // JPA 스펙상 만들어 놓아야 한다.

    public Address(String city, String street, String zipcode) { // 생성자로만 값을 변경할 수 있도록 한다.
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
