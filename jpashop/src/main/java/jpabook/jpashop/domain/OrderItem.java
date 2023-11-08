package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) { // 주문 상품 생성
        OrderItem orderItem = new OrderItem(); // 주문 상품 생성
        orderItem.setItem(item); // 주문 상품 설정
        orderItem.setOrderPrice(orderPrice); // 주문 가격 설정
        orderItem.setCount(count); // 주문 수량 설정
        item.removeStock(count); // 주문 상품의 재고를 줄인다.
        return orderItem; // 주문 상품 반환
    }

    //==비지니스 로직==//
    public void cancel() {
        getItem().addStock(count); // 재고 수량을 원복한다.
    }

    //==조회 로직==//
    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() { return getOrderPrice() * getCount(); }
}
