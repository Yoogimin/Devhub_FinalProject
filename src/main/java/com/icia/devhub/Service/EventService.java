package com.icia.devhub.Service;

import com.icia.devhub.dao.EventRepository;
import com.icia.devhub.dao.MemberRepository;
import com.icia.devhub.dto.Event.EventDTO;
import com.icia.devhub.dto.Event.EventEntity;
import com.icia.devhub.dto.Member.MemberEntity;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service // 해당 클래스가 서비스 레이어임을 나타냅니다.
@RequiredArgsConstructor // Lombok을 사용하여 final 필드에 대한 생성자를 자동으로 생성합니다.
public class EventService {

    private final HttpSession session; // HttpSession 객체를 주입받습니다.
    private final EventRepository eventRepository; // EventRepository 주입
    private final MemberRepository memberRepository;

    public void event(EventDTO dto) {
        // 세션에서 닉네임 값 가져오기
        String loginId = (String) session.getAttribute("loginId");

        // 닉네임 값으로 MemberEntity 찾기
        MemberEntity member = memberRepository.findById(loginId)
                .orElseThrow(() -> new RuntimeException("회원 정보를 찾을 수 없습니다."));

        // EventEntity 생성 및 설정
        EventEntity entity = EventEntity.toEntity(dto);
        entity.setMember(member);

        // 이벤트 저장
        eventRepository.save(entity);

        // 포인트 업데이트
        member.setMPoint(member.getMPoint() + dto.getPOINTS());

        // 회원 정보 저장
        memberRepository.save(member);
    }

    public List<String> getAttendanceDays(String userId) {
        List<EventEntity> events = eventRepository.findByMember_MId(userId);
        return events.stream()
                .map(EventEntity::getIDATE)
                .collect(Collectors.toList());
    }
}
