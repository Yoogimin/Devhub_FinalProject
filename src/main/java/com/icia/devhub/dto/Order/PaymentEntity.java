package com.icia.devhub.dto.Order;

import com.icia.devhub.dto.Member.MemberEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "DEV_PAYMENT")
@SequenceGenerator(name="PMT_SEQ_GENERATOR", sequenceName="PMT_SEQ", allocationSize=1)
public class PaymentEntity {
    //결제 ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PMT_SEQ_GENERATOR")
    private int PId;

    //결제 상품
    @ManyToOne
    @JoinColumn(name = "PPId")
    private ProductEntity product;

    //결제자
    @ManyToOne
    @JoinColumn(name = "PMId")
    private MemberEntity member;

    //결제 날짜
    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime PDate;

    public static PaymentEntity toEntity(PaymentDTO dto) {
        PaymentEntity entity = new PaymentEntity();
        ProductEntity productEntity = new ProductEntity();
        productEntity.setPId(dto.getPPId());

        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMId(dto.getPMId());

        entity.setPId(dto.getPId());
        entity.setProduct(productEntity);
        entity.setMember(memberEntity);
        entity.setPDate(dto.getPDate());

        return entity;
    }
}