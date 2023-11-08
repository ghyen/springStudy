package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

@Entity
@DiscriminatorValue("M") // DTYPE에 들어갈 값을 지정해준다.
@Getter @Setter
public class Movie extends Item {

    private String director;
    private String actor;
}
