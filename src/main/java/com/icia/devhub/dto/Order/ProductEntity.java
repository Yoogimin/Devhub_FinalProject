package com.icia.devhub.dto.Order;

import com.icia.devhub.dto.Member.MemberEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "DEV_PRODUCT")
@SequenceGenerator(name="PRD_SEQ_GENERATOR", sequenceName="CTT_SEQ", allocationSize=1)
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRD_SEQ_GENERATOR")
    private int PId;

    //상품 이름
    @Column(nullable = false, length = 1000)
    private String PName;

    //상품 가격
    @Column
    private int PPrice;

    //상품 카테고리
    @Column
    private String PCategory;

    //상품 설명
    @Column
    private String PExplain;

    @ManyToOne
    @JoinColumn(name = "PCId")
    private CategoryEntity category;

    @OneToMany(mappedBy = "product")
    private List<PaymentEntity> payments;

    @OneToMany(mappedBy = "product")
    private List<CartEntity> carts;

    @OneToMany(mappedBy = "product")
    private List<HistoryEntity> historys;

    @ManyToOne
    @JoinColumn(name="PMId")
    private MemberEntity member;


    public static ProductEntity toEntity(ProductDTO dto) {
        ProductEntity entity = new ProductEntity();
        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setCategory(dto.getPCId());
        MemberEntity member = new MemberEntity();
        member.setMId(dto.getPMId());

        entity.setPId(dto.getPId());
        entity.setCategory(categoryEntity);
        entity.setPName(dto.getPName());
        entity.setPPrice(dto.getPPrice());
        entity.setPCategory(dto.getPCategory());
        entity.setPExplain(dto.getPExplain());
        entity.setMember(member);
        return entity;
    }
}