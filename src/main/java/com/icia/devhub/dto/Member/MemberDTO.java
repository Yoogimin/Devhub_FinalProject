package com.icia.devhub.dto.Member;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MemberDTO {
    private String MId;
    private String MPw;
    private String MName;
    private String MBirth;
    private String MGender;
    private String MEmail;
    private String MPhone;

    private MultipartFile MProfile;
    private String MProfileName;

    private int MPoint;

    //entity -> dto 바꾸는 메소드
    public static MemberDTO toDTO(MemberEntity entity) {
        MemberDTO dto = new MemberDTO();

        dto.setMId(entity.getMId());
        dto.setMPw(entity.getMPw());
        dto.setMName(entity.getMName());
        dto.setMBirth(entity.getMBirth());
        dto.setMGender(entity.getMGender());
        dto.setMEmail(entity.getMEmail());
        dto.setMPhone(entity.getMPhone());
        dto.setMProfileName(entity.getMProfileName());
        dto.setMPoint(entity.getMPoint());

        return dto;
    }
}