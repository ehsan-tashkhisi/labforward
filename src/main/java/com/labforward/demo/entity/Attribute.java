package com.labforward.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Attribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean required;

    @JoinColumn(name = "category_id", nullable = false)
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Category category;

    @ManyToOne
    @JoinColumn(name = "type_id", nullable = false)
    private AttributeType attributeType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Attribute))
            return false;

        Attribute other = (Attribute) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
