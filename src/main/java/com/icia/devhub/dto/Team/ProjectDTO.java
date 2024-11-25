package com.icia.devhub.dto.Team;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectDTO {
    //프로젝트 ID
    private int PId;

    //팀 ID
    private int PTId;

    //프로젝트 이름
    private String PName;

    //프로젝트 작성자
    private String PMWriter;

    //프로젝트 내용
    private String PContact;

    //프로젝트 생성일
    private LocalDateTime PDate;

    //프로젝트 조회수
    private int PHit;

    //프로젝트 타입
    private String PType;

    //프로젝트 작성자 프로필
    private String PProfile;

    //프로젝트 작성자 이메일
    private String PEmail;

    public static ProjectDTO toDTO(ProjectEntity entity) {
        ProjectDTO dto = new ProjectDTO();

        dto.setPId(entity.getPId());
        dto.setPTId(entity.getTeam().getTId());
        dto.setPMWriter(entity.getMember().getMId());
        dto.setPContact(entity.getPContact());
        dto.setPName(entity.getPName());
        dto.setPDate(entity.getPDate());
        dto.setPHit(entity.getPHit());
        dto.setPType(entity.getPType());
        dto.setPProfile(entity.getPProfile());
        dto.setPEmail(entity.getPEmail());

        return dto;
    }
}