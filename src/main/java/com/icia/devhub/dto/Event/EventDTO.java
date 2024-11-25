package com.icia.devhub.dto.Event;

import com.icia.devhub.dto.Order.CartDTO;
import com.icia.devhub.dto.Order.CartEntity;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.time.LocalDateTime;

@Data
@RequiredArgsConstructor
public class EventDTO {
    private int IId; // 이벤트 ID
    private String IDATE; // 이벤트 날짜
    private int POINTS; // 포인트
    private LocalDateTime PTIME; // 포인트 시간
    private String IMID; // 회원 ID

    public static EventDTO toDTO(EventEntity entity) {
        EventDTO dto = new EventDTO();

        dto.setIId(entity.getIId());
        dto.setIDATE(entity.getIDATE());
        dto.setPOINTS(entity.getPOINTS());
        dto.setPTIME(entity.getPTIME());
        dto.setIMID(entity.getMember().getMId());

        return dto;
    }
}