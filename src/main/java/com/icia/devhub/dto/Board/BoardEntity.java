package com.icia.devhub.dto.Board;

import com.icia.devhub.dto.Member.MemberEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name="DEV_BOARD")
@SequenceGenerator(name="BOA_SEQ_GENERATOR", sequenceName="BOA_SEQ", allocationSize=1)
public class BoardEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOA_SEQ_GENERATOR")
    private int BNum;

    //게시글 제목
    @Column(nullable = false, length = 100)
    private String BTitle;

    //게시글
    @Column(nullable = false)
    private String BContent;

    //게시글 작성날
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime BDate;

    //게시글 조회수
    @Column
    private int BHit;

    //게시글 첨부파일 이름
    @Column
    private String BFileName;

    //게시글 타입
    @Column
    private String BType;

    //작성자
    @ManyToOne
    @JoinColumn(name = "BWriter")
    private MemberEntity member;

    @OneToMany(mappedBy = "board")
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "board")
    private List<CodeEntity> codes;

    //entity to dto
    public static BoardEntity toEntity(BoardDTO dto) {
        BoardEntity entity = new BoardEntity();
        MemberEntity member = new MemberEntity();
        member.setMId(dto.getBWriter());

        entity.setBNum(dto.getBNum());
        entity.setBTitle(dto.getBTitle());
        entity.setBContent(dto.getBContent());
        entity.setBDate(dto.getBDate());
        entity.setBHit(dto.getBHit());
        entity.setBFileName(dto.getBFileName());
        entity.setBType(dto.getBType());
        entity.setMember(member);

        return entity;
    }
}