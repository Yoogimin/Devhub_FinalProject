package com.icia.devhub.dao;

import com.icia.devhub.dto.Team.ProjectEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Integer> {
    // 특정 BNum 값을 가진 게시물의 조회수를 1 증가시킵니다.
    @Modifying // 데이터베이스를 수정하는 쿼리임을 나타냅니다.
    @Transactional // 트랜잭션 관리가 필요함을 나타냅니다.
    @Query("UPDATE ProjectEntity P SET P.PHit = P.PHit + 1 WHERE P.PId = :PId")
    void increaseHit(@Param("PId") int PId); // BNum 파라미터를 받아 해당 게시물의 조회수를 증가시킵니다.

    List<ProjectEntity> findByTeam_TIdOrderByPIdDesc(int tId);

    List<ProjectEntity> findByPTypeContainingOrderByPIdDesc(String keyword);

    List<ProjectEntity> findByPContactContainingOrderByPIdDesc(String keyword);

    List<ProjectEntity> findByPNameContainingOrderByPIdDesc(String keyword);

    void deleteByMember_MId(String mId);
}
