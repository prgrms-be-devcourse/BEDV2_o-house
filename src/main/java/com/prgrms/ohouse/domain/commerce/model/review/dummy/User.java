package com.prgrms.ohouse.domain.commerce.model.review.dummy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;

@Entity(name = "users")
@Getter
public class User {
    @Id
    @GeneratedValue
    private Long id;
}
