package com.prgrms.ohouse.domain.commerce.model.review.dummy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;

@Entity
@Getter
public
class Products {
    @Id
    @GeneratedValue
    private Long id;
}
