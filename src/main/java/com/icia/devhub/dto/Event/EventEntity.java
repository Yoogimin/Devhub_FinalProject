package com.icia.devhub.dto.Event;

import com.icia.devhub.dto.Member.MemberEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "DEV_Event")
public class EventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int IId; // 이벤트 ID

    @Column(nullable = false, length = 100)
    private String IDATE; // 이벤트 날짜

    @Column(nullable = false)
    private int POINTS; // 포인트

    // JPA 애노테이션: 이 필드를 데이터베이스의 칼럼과 매핑하며, 업데이트 시 값이 변경되지 않도록 설정합니다.
    @Column(updatable = false)
    // Hibernate 애노테이션: 엔티티가 처음 저장될 때 이 필드에 현재 날짜와 시간을 자동으로 설정합니다.
    @CreationTimestamp
    // PTIME 필드 선언: 포인트 획득 날짜와 시간을 기록합니다.
    private LocalDateTime PTIME;

    @ManyToOne
    @JoinColumn(name = "IMID")
    private MemberEntity member; // 회원 엔티티

    public static EventEntity toEntity(EventDTO dto) {
        EventEntity entity = new EventEntity();
        MemberEntity member = new MemberEntity();

        entity.setIId(dto.getIId());
        entity.setIDATE(dto.getIDATE());
        entity.setPOINTS(dto.getPOINTS());
        entity.setPTIME(dto.getPTIME());
        entity.setMember(member);

        return entity;
    }
}
