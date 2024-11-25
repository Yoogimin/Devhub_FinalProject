package com.icia.devhub.dto.Team;

import com.icia.devhub.dto.Member.MemberEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "DEV_TEAM")
@SequenceGenerator(name = "TAM_SEQ_GENERATOR", sequenceName = "TAM_SEQ", allocationSize = 1)
public class TeamEntity {
    //팀 ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TAM_SEQ_GENERATOR")
    private int TId;

    //팀 이름
    @Column(nullable = false, length = 100)
    private String TName;

    //급여
    @Column(nullable = false, length = 50)
    private String TSalary;

    //경력
    @Column(nullable = false, length = 10)
    private String TExperience;

    //근무방식
    @Column(nullable = false, length = 20)
    private String TWorkType;

    //계약형태
    @Column(nullable = false, length = 20)
    private String TContract;

    //학력
    @Column(nullable = false, length = 20)
    private String TEducation;

    //급여일
    @Column(nullable = false, updatable = false, length = 10)
    private String TPayday;

    //보유기술
    @Column(nullable = false, length = 100)
    private String TSkill;

    //인원수
    @Column(nullable = false, length = 100)
    private String THCount;

    //팀 기간
    @Column(nullable = false, length = 100)
    private String TDuration;

    @OneToMany(mappedBy = "team")
    private List<ProjectEntity> projects;

    //멤버 ID
    @ManyToOne
    @JoinColumn(name = "TMId", nullable = false)
    private MemberEntity member;

    public static TeamEntity toEntity(TeamDTO dto) {
        TeamEntity entity = new TeamEntity();
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMId(dto.getTMId());

        entity.setTId(dto.getTId());
        entity.setTName(dto.getTName());
        entity.setTSalary(dto.getTSalary());
        entity.setTExperience(dto.getTExperience());
        entity.setTWorkType(dto.getTWorkType());
        entity.setTContract(dto.getTContract());
        entity.setTEducation(dto.getTEducation());
        entity.setTPayday(dto.getTPayday());
        entity.setTSkill(dto.getTSkill());
        entity.setTHCount(dto.getTHCount());
        entity.setTDuration(dto.getTDuration());
        entity.setMember(memberEntity);

        return entity;
    }
}