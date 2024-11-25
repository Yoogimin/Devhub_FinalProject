package com.icia.devhub.dto.Order;

import com.icia.devhub.dto.Member.MemberEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "DEV_HISTORY")
@SequenceGenerator(name = "DHT_SEQ_GENERATOR", sequenceName = "DHT_SEQ", allocationSize = 1)
public class HistoryEntity {
    //열람 기록 ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DHT_SEQ_GENERATOR")
    private int HId;

    //열람 상품
    @ManyToOne
    @JoinColumn(name = "HPId")
    private ProductEntity product;

    //열람한 회원
    @ManyToOne
    @JoinColumn(name = "HMId")
    private MemberEntity member;

    //열람 날짜
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime HDate;

    //열람시 사용된 포인트
    @Column(nullable = false)
    private int HDPoint;

    public static HistoryEntity toEntity(HistoryDTO dto) {
        HistoryEntity history = new HistoryEntity();
        ProductEntity productEntity = new ProductEntity();
        productEntity.setPId(dto.getHPId());

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMId(dto.getHMId());

        history.setHId(dto.getHId());
        history.setProduct(productEntity);
        history.setMember(memberEntity);
        history.setHDate(dto.getHDate());
        history.setHDPoint(dto.getHDPoint());

        return history;
    }
}