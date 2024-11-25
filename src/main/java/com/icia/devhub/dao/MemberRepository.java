package com.icia.devhub.dao;

import com.icia.devhub.dto.Member.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<MemberEntity, String> {

}