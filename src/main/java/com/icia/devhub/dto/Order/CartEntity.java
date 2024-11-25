package com.icia.devhub.dto.Order;

import com.icia.devhub.dto.Member.MemberEntity;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "DEV_CART")
@SequenceGenerator(name = "DCT_SEQ_GENERATOR", sequenceName = "DCT_SEQ", allocationSize = 1)
public class CartEntity {
    //장바구니 ID
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DCT_SEQ_GENERATOR")
    private int CId;

    //회원 장바구니
    @ManyToOne
    @JoinColumn(name = "CMId")
    private MemberEntity member;

    //상품 장바구니
    @ManyToOne
    @JoinColumn(name = "CPId")
    private ProductEntity product;

    public static CartEntity toEntity(CartDTO dto) {
        CartEntity cart = new CartEntity();
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.setMId(dto.getCMId());

        ProductEntity productEntity = new ProductEntity();
        productEntity.setPId(dto.getCPId());

        cart.setCId(dto.getCId());
        cart.setMember(memberEntity);
        cart.setProduct(productEntity);

        return cart;
    }
}