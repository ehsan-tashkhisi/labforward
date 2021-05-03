package com.labforward.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;
    @NotBlank
    private String name;

    @OneToMany(mappedBy = "category")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Attribute> attributes = new ArrayList<>();

    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
        attribute.setCategory(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Category))
            return false;
        Category other = (Category) o;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
