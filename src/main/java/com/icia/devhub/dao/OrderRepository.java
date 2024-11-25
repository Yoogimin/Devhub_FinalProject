package com.icia.devhub.dao;


import com.icia.devhub.dto.Order.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<ProductEntity, String> {


    List<ProductEntity> findAllByMember_MIdOrderByPId(String MId);

    List<ProductEntity> findByMember_MIdAndPExplainAndPNameOrderByPIdAsc(String member_MId, String PExplain, String PName);



    ProductEntity findByPId(int PId);
}