package com.icia.devhub.dto.Team;

import lombok.Data;

@Data
public class ResumeDTO {
    //이력서 ID
    private int RId;

    //프로젝트 ID
    private int RPId;

    //회원 ID
    private String RMId;

    //제목
    private String RTitle;

    //경력
    private String RExperience;

    //학력
    private String REducation;

    //기술
    private String RSkill;

    //성장과정
    private String RGProcess;

    //성격 및 생활신조
    private String RPersonality;

    //장점 및 단점
    private String RAD;

    //지원동기
    private String RMotive;

    //회사 업무에 대한 자세 및 포부
    private String RAspiration;

    public static ResumeDTO toDTO(ResumeEntity entity) {
        ResumeDTO dto = new ResumeDTO();

        dto.setRId(entity.getRId());
        dto.setRPId(entity.getProject().getPId());
        dto.setRMId(entity.getMember().getMId());
        dto.setRTitle(entity.getRTitle());
        dto.setRExperience(entity.getRExperience());
        dto.setREducation(entity.getREducation());
        dto.setRSkill(entity.getRSkill());
        dto.setRGProcess(entity.getRGProcess());
        dto.setRPersonality(entity.getRPersonality());
        dto.setRAD(entity.getRAD());
        dto.setRMotive(entity.getRMotive());
        dto.setRAspiration(entity.getRAspiration());

        return dto;
    }
}