package com.icia.devhub.dao;

import com.icia.devhub.dto.Order.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Integer> {
    void deleteByMember_MId(String mId);
}
