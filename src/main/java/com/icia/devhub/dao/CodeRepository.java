package com.icia.devhub.dao;

import com.icia.devhub.dto.Board.CodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CodeRepository extends JpaRepository<CodeEntity, Integer> {

    List<CodeEntity> findByBoard_BNum(int BNum);

    List<CodeEntity> findByBoard_BNumOrderByDTNumDesc(int bNum);
}