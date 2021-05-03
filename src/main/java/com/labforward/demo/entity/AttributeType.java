package com.labforward.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
public class AttributeType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotBlank
    private String name;

    @Enumerated(EnumType.STRING)
    private ValueType valueType;

    @Enumerated(EnumType.STRING)

    private UnitType unitType;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof AttributeType))
            return false;

        AttributeType other = (AttributeType) o;

        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public enum ValueType {
        STRING,
        INTEGER,
        DOUBLE,
        DATE

    }

    public enum UnitType {
        METER,
        DATE,
        DOLOR,
        STRING
    }
}
