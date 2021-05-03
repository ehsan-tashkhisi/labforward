package com.labforward.demo.repository;

import com.labforward.demo.entity.Category;
import com.labforward.demo.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {

    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value = "3000")})
    @Query("SELECT i FROM Item i LEFT JOIN ItemAttribute ia on i.id = ia.item.id  WHERE i.id=:itemId")
    @Lock(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
    Optional<Item> findItemByIdForUpdate(@Param("itemId") Long itemId);

    Page<Item> findByCategory(Category category, Pageable pageable);
}


