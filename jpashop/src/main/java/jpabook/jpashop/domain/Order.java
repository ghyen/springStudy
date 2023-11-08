package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL) // 일대일 관계에서는 기본적으로 지연로딩을 사용한다.
    @JoinColumn(name = "delivery_id") // 일대일 관계에서는 외래키를 누가 갖고 있어도 상관없다.
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //==연관관계 메서드==// 양방향 연관관계에서는 연관관계 편의 메서드를 작성하는 것이 좋다.
    public void setMember(Member member) { // 연관관계 편의 메서드
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) { // 연관관계 편의 메서드
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) { // 연관관계 편의 메서드
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 메서드==// 생성 메서드를 사용하면 생성 시점에 추가로 로직을 실행할 수 있다.
    // 주문 생성 시점에는 주문 상태를 ORDER로 설정한다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order(); // 주문 생성
        order.setMember(member); // 주문 회원 설정
        order.setDelivery(delivery); // 배송 정보 설정
        for (OrderItem orderItem : orderItems) { // 주문 상품 설정
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER); // 주문 상태 설정
        order.setOrderDate(LocalDateTime.now()); // 주문 시간 설정
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문 취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) { // 이미 배송이 완료된 상품은 취소할 수 없다.
            throw new IllegalStateException("이미 배송이 완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL); // 주문 상태를 취소로 변경한다.
        for (OrderItem orderItem : orderItems) { // 주문 상품의 재고를 취소된 만큼 증가시킨다.
            orderItem.cancel();
        }
    }

    //==조회 로직==// 전체 주문 가격을 조회한다.
    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) { // 주문 상품의 가격을 모두 더한다.
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

}
