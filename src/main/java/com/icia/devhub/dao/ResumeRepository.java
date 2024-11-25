package com.icia.devhub.dao;

import com.icia.devhub.dto.Team.ResumeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<ResumeEntity, Integer> {
    void deleteByMember_MId(String mId);
}