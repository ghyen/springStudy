package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) {
            em.persist(item);
        } else {
            em.merge(item); // update와 비슷한 기능
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
            .getResultList();
    }

    // public List<Item> findByName(String name) {
    //     return em.createQuery("select i from Item i where i.name = :name", Item.class)
    //             .setParameter("name", name)
    //             .getResultList();
    // }

    // public List<Item> findByPrice(int price) {
    //     return em.createQuery("select i from Item i where i.price = :price", Item.class)
    //             .setParameter("price", price)
    //             .getResultList();
    // }

    // public List<Item> findByStockQuantity(int stockQuantity) {
    //     return em.createQuery("select i from Item i where i.stockQuantity = :stockQuantity", Item.class)
    //             .setParameter("stockQuantity", stockQuantity)
    //             .getResultList();
    // }

}
