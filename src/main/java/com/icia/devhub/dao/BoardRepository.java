package com.icia.devhub.dao;

import com.icia.devhub.dto.Board.BoardEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// BoardRepository 인터페이스는 JpaRepository를 확장하여 데이터베이스 작업을 수행합니다.
public interface BoardRepository extends JpaRepository<BoardEntity, Integer> {

    // 모든 BoardEntity를 BNum 필드로 내림차순 정렬하여 반환합니다.
    List<BoardEntity> findAllByOrderByBNumDesc();

    // 특정 회원 ID를 포함하는 BoardEntity를 BNum 필드로 내림차순 정렬하여 반환합니다.
    List<BoardEntity> findByMember_MIdContainingOrderByBNumDesc(String keyword);

    // 특정 제목을 포함하는 BoardEntity를 BNum 필드로 내림차순 정렬하여 반환합니다.
    List<BoardEntity> findByBTitleContainingOrderByBNumDesc(String keyword);

    // 특정 내용을 포함하는 BoardEntity를 BNum 필드로 내림차순 정렬하여 반환합니다.
    List<BoardEntity> findByBContentContainingOrderByBNumDesc(String keyword);

    // 특정 BNum 값을 가진 게시물의 조회수를 1 증가시킵니다.
    @Modifying // 데이터베이스를 수정하는 쿼리임을 나타냅니다.
    @Transactional // 트랜잭션 관리가 필요함을 나타냅니다.
    @Query("UPDATE BoardEntity B SET B.BHit = B.BHit + 1 WHERE B.BNum = :BNum")
    void increaseBHit(@Param("BNum") int BNum); // BNum 파라미터를 받아 해당 게시물의 조회수를 증가시킵니다.

    List<BoardEntity> findByMember_MId(String mId);

    void deleteByMember_MId(String mId);
}
