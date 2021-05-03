package com.labforward.demo.repository;

import com.labforward.demo.entity.ItemAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ValueRepository extends JpaRepository<ItemAttribute, Long> {
}
