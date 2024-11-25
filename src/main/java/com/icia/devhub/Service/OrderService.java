package com.icia.devhub.Service;

import com.icia.devhub.dao.CategoryRepository;
import com.icia.devhub.dao.MemberRepository;
import com.icia.devhub.dao.OrderRepository;
import com.icia.devhub.dto.Member.MemberEntity;
import com.icia.devhub.dto.Order.CategoryEntity;
import com.icia.devhub.dto.Order.ProductDTO;
import com.icia.devhub.dto.Order.ProductEntity;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final MemberRepository mrepo; // MemberRepository 객체를 주입받습니다.
    private final HttpSession session; // HttpSession 객체를 주입받습니다.
    private final OrderRepository orepo; // OrderRepository 객체를 주입받습니다.
    private final CategoryRepository crepo; // CategoryRepository 객체를 주입받습니다.
    private final EmailService esvc; // EmailService 객체를 주입받습니다.

    public int chargePoints(String MId, int MPoint, HttpSession session, ProductDTO order, String fromAddress) {
        Optional<MemberEntity> entity = mrepo.findById(MId); // 회원을 아이디로 조회합니다
        if (entity.isPresent()) { // 회원이 존재하면
            MemberEntity memberEntity = entity.get(); // 회원 엔티티를 가져옵니다.
            int currentPoints = memberEntity.getMPoint(); // 현재 포인트를 가져옵니다.
            memberEntity.setMPoint(currentPoints + MPoint); // 포인트를 충전합니다.
            mrepo.save(memberEntity); // 엔티티를 저장합니다.
            session.setAttribute("loginMPoint", memberEntity.getMPoint()); // 세션에 포인트를 업데이트합니다.

            // 카테고리 엔티티 저장 또는 조회
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setCategoryName(order.getPCategory());
            crepo.save(categoryEntity);

            // 주문 정보 생성 및 저장
            ProductEntity productEntity = ProductEntity.toEntity(order);
            productEntity.setMember(memberEntity); // MemberEntity 설정
            productEntity.setCategory(categoryEntity); // CategoryEntity 설정
            System.out.println(ProductDTO.toDTO(productEntity));
            ProductEntity savedEntity = orepo.save(productEntity);
            ProductDTO savedOrder = ProductDTO.toDTO(savedEntity);

            // 이메일 전송
            esvc.sendPurchaseDetails(fromAddress, session, savedOrder);

            return memberEntity.getMPoint(); // 충전 후 포인트 반환
        }
        return 0; // 회원이 존재하지 않으면 0 반환
    }


    public int deductPoints(String MId, int MPoint) {
        Optional<MemberEntity> member = mrepo.findById(MId);
        if (member.isPresent()) { // 회원이 존재하면
            MemberEntity memberEntity = member.get();
            int currentPoints = memberEntity.getMPoint();
            if (currentPoints >= MPoint) { // 현재 포인트가 차감할 포인트보다 크거나 같으면
                memberEntity.setMPoint(currentPoints - MPoint); // 포인트 차감
                mrepo.save(memberEntity); // 엔티티 저장
                session.setAttribute("loginMPoint", memberEntity.getMPoint()); // 세션에 포인트 업데이트
                return MPoint; // 차감한 포인트 반환
            }
        }
        return 0; // 회원이 존재하지 않거나 포인트가 부족하면 0 반환
    }

    public int getMemberPoints(String mId) {
        Optional<MemberEntity> entity = mrepo.findById(mId);
        if (entity.isPresent()) {
            return entity.get().getMPoint();
        } else {
            return 0;
        }
    }

    public List<ProductDTO> phistory(String PExplain, String PName, String MId) {
        Optional<MemberEntity> optionalMember = mrepo.findById(MId);
        if (optionalMember.isPresent()) {
            MemberEntity member = optionalMember.get();

            // ProductEntity를 저장하기 전에 MemberEntity가 저장되어 있는지 확인
            if (!mrepo.existsById(member.getMId())) {
                mrepo.save(member);
            }

            List<ProductEntity> phistory;
            if (PExplain.isEmpty() && PName.isEmpty()) {
                phistory = orepo.findAllByMember_MIdOrderByPId(MId);
            } else {
                phistory = orepo.findByMember_MIdAndPExplainAndPNameOrderByPIdAsc(MId, PExplain, PName);
            }

            List<ProductDTO> productDTOList = new ArrayList<>();
            for (ProductEntity product : phistory) {
                productDTOList.add(ProductDTO.toDTO(product));
            }
            return productDTOList;
        } else {
            return new ArrayList<>();
        }
    }

    public String insertProduct(String loginId, HttpSession session, ProductDTO order, String fromAddress) {
        Optional<MemberEntity> entity = mrepo.findById(loginId); // 회원을 아이디로 조회합니다
        if (entity.isPresent()) { // 회원이 존재하면
            MemberEntity memberEntity = entity.get(); // 회원 엔티티를 가져옵니다.
            mrepo.save(memberEntity); // 엔티티를 저장합니다.

            // 카테고리 엔티티 저장 또는 조회
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setCategoryName(order.getPCategory());
            categoryEntity = crepo.save(categoryEntity);

            // 주문 정보 생성 및 저장
            ProductEntity productEntity = ProductEntity.toEntity(order);
            productEntity.setMember(memberEntity); // MemberEntity 설정
            productEntity.setCategory(categoryEntity); // CategoryEntity 설정
            ProductEntity savedEntity = orepo.save(productEntity);

            // 이메일 전송
            esvc.sendPurchaseDetails(fromAddress, session, ProductDTO.toDTO(savedEntity));

            return memberEntity.getMId();
        }
        return null;
    }
}
