package com.icia.devhub.dto.Team;

import com.icia.devhub.dto.Member.MemberEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "DEV_PROJECT")
@SequenceGenerator(name = "PRJ_SEQ_GENERATOR", sequenceName = "PRJ_SEQ", allocationSize = 1)
public class ProjectEntity {
    //프로젝트 ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRJ_SEQ_GENERATOR")
    private int PId;

    //팀 ID
    @ManyToOne
    @JoinColumn(name = "PTId", nullable = false)
    private TeamEntity team;

    //프로젝트 이름
    @Column(nullable = false, length = 100)
    private String PName;

    //프로젝트 작성자
    @ManyToOne
    @JoinColumn(name = "PMWriter", nullable = false)
    private MemberEntity member;

    //프로젝트 생성일
    @Column(updatable = false)
    private LocalDateTime PDate;

    //프로젝트 조회수
    @Column(nullable = false)
    private int PHit;

    //프로젝트 설명
    @Column(nullable = false, length = 1000)
    private String PContact;

    //프로젝트 타입
    @Column(nullable = false, length = 20)
    private String PType;

    //프로젝트 작성자 프로필
    @Column(nullable = false, length = 30)
    private String PProfile;

    @Column(nullable = false, length = 30)
    private String PEmail;

    @OneToMany(mappedBy = "project")
    private List<ResumeEntity> resumes;

    public static ProjectEntity toEntity(ProjectDTO dto) {
        ProjectEntity entity = new ProjectEntity();
        TeamEntity teamEntity = new TeamEntity();
        teamEntity.setTId(dto.getPTId());

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMId(dto.getPMWriter());

        entity.setPId(dto.getPId());
        entity.setMember(memberEntity);
        entity.setTeam(teamEntity);
        entity.setPName(dto.getPName());
        entity.setPType(dto.getPType());
        entity.setPContact(dto.getPContact());
        entity.setPHit(dto.getPHit());
        entity.setPDate(dto.getPDate());
        entity.setPProfile(dto.getPProfile());
        entity.setPEmail(dto.getPEmail());

        return entity;
    }
}