package com.icia.devhub.Controller;

import com.icia.devhub.Service.EventService;
import com.icia.devhub.dto.Event.EventDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService; // EventService를 주입받습니다.

    private final HttpSession session; // HttpSession 객체를 주입받습니다.

    @GetMapping("/attend")
    public String attend() {
        return "attendance"; // "attendance.html" 템플릿을 반환
    }

    @PostMapping("/EventAtt")
    public @ResponseBody void event(@ModelAttribute EventDTO dto) {
        System.out.println(dto);
        eventService.event(dto);
    }

    @GetMapping("/getAttendanceDays")
    public @ResponseBody List<String> getAttendanceDays() {
        String userId = (String) session.getAttribute("loginId");
        return eventService.getAttendanceDays(userId);
    }
}
