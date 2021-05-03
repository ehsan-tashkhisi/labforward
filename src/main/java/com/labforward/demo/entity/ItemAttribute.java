package com.labforward.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class ItemAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String value;

    @ManyToOne
    @JoinColumn(name = "attribute_id")
    @ToString.Exclude
    private Attribute attribute;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", nullable = false)
    @ToString.Exclude
    @JsonIgnore
    private Item item;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof ItemAttribute))
            return false;

        ItemAttribute other = (ItemAttribute) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
