package com.icia.devhub.dto.Member;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="DEV_LOGIN")
@SequenceGenerator(name="DLN_SEQ_GENERATOR", sequenceName="DLN_SEQ", allocationSize=1)
public class LoginEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DLN_SEQ_GENERATOR")
    private int LId; // 로그인 ID

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime LDate; // 로그인 날짜 및 시간

    @Column(nullable = false, length = 100)
    private String IPAddr; // IP 주소

    @Column(nullable = false, length = 100)
    private String location; // 위치

    @Column(nullable = false, length = 100)
    private String isp; // ISP 정보

    @ManyToOne
    @JoinColumn(name = "LMId")
    private MemberEntity member; // 회원 엔티티

    // LoginDTO를 LoginEntity로 변환하는 메서드
    public static LoginEntity toEntity(LoginDTO dto) {
        LoginEntity entity = new LoginEntity();
        MemberEntity member = new MemberEntity();
        member.setMId(dto.getLMId());

        entity.setLId(dto.getLId());
        entity.setLDate(dto.getLDate());
        entity.setIPAddr(dto.getIPAddr());
        entity.setLocation(dto.getLocation());
        entity.setIsp(dto.getIsp());
        entity.setMember(member);

        return entity;
    }
}
