package com.icia.devhub.Service;

import com.icia.devhub.dao.HistoryRepository;
import com.icia.devhub.dao.MemberRepository;
import com.icia.devhub.dao.OrderRepository;
import com.icia.devhub.dto.Member.MemberEntity;
import com.icia.devhub.dto.Order.HistoryDTO;
import com.icia.devhub.dto.Order.HistoryEntity;
import com.icia.devhub.dto.Order.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final MemberRepository mrepo;
    private final HistoryRepository hrepo;
    private final OrderRepository orepo;




    //열람 내역 조회
    public List<HistoryDTO> getHistory(String mid) {
        // 사용자 ID로 히스토리 엔티티 목록을 조회합니다.
        List<HistoryEntity> historyEntities = hrepo.findByMember_MId(mid);
        // 히스토리 엔티티 목록을 DTO 목록으로 변환합니다.
        return historyEntities.stream()
                .map(entity -> {
                    HistoryDTO dto = new HistoryDTO();
                    dto.setHId(entity.getHId());
                    dto.setHMId(entity.getMember().getMId());
                    dto.setHDate(entity.getHDate());
                    dto.setHDPoint(entity.getHDPoint());
                    // 엔티티에 제품 정보가 있을 경우 제품 ID를 설정합니다.
                    if (entity.getProduct() != null) {
                        dto.setHPId(entity.getProduct().getPId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // history db 생성
    public void saveHistory(String mid, List<Integer> pids) {
        // 사용자 ID로 사용자 엔티티를 조회합니다.
        MemberEntity member = mrepo.findById(mid).orElseThrow(() -> new IllegalArgumentException("Invalid member ID: " + mid));
        // 제품 ID 목록을 순회하며 히스토리 엔티티를 생성하고 저장합니다.
        for (Integer pid : pids) {
            ProductEntity product = orepo.findByPId(pid);
            HistoryEntity history = new HistoryEntity();
            history.setMember(member);
            history.setProduct(product);
            history.setHDate(LocalDateTime.now());
            history.setHDPoint(200);  // 포인트는 예시로 200으로 설정

            // 히스토리 엔티티를 저장합니다.
            hrepo.save(history);
        }
    }
}
