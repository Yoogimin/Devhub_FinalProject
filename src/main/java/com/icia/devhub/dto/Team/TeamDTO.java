package com.icia.devhub.dto.Team;

import lombok.Data;

@Data
public class TeamDTO {
    //팀 ID
    private int TId;

    //멤버 ID
    private String TMId;

    //팀 이름
    private String TName;

    //급여
    private String TSalary;

    //경력
    private String TExperience;

    //근무방식
    private String TWorkType;

    //계약형태
    private String TContract;

    //학력
    private String TEducation;

    //급여일
    private String TPayday;

    //보유기술
    private String TSkill;

    //인원수
    private String THCount;

    //팀 기간
    private String TDuration;

    public static TeamDTO toDTO(TeamEntity entity) {
        TeamDTO dto = new TeamDTO();

        dto.setTId(entity.getTId());
        dto.setTMId(entity.getMember().getMId());
        dto.setTName(entity.getTName());
        dto.setTSalary(entity.getTSalary());
        dto.setTExperience(entity.getTExperience());
        dto.setTWorkType(entity.getTWorkType());
        dto.setTContract(entity.getTContract());
        dto.setTEducation(entity.getTEducation());
        dto.setTPayday(entity.getTPayday());
        dto.setTSkill(entity.getTSkill());
        dto.setTHCount(entity.getTHCount());
        dto.setTDuration(entity.getTDuration());

        return dto;
    }
}