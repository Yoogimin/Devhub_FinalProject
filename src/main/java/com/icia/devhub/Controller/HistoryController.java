package com.icia.devhub.Controller;

import com.icia.devhub.Service.HistoryService;
import com.icia.devhub.dto.Order.HistoryDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService hsvc;

    /**
     * 사용자의 열람 내역을 가져옵니다.
     * 세션에서 로그인된 사용자의 ID를 가져와 히스토리 서비스를 통해 히스토리를 조회합니다.
     */
    @GetMapping("/uihistory")
    @ResponseBody
    public List<HistoryDTO> getHistory(HttpSession session) {
        // 세션에서 로그인된 사용자의 ID를 가져옵니다.
        String MId = (String) session.getAttribute("loginId");
        // 서비스 계층을 호출하여 히스토리 목록을 가져옵니다.
        return hsvc.getHistory(MId);
    }

    /**
     * 사용자의 히스토리를 저장합니다.
     * 세션에서 로그인된 사용자의 ID를 가져와 히스토리 서비스를 통해 히스토리를 저장합니다.
     */
    @PostMapping("/addHistory")
    public String addHistory(HttpSession session, @RequestParam List<Integer> pids) {
        // 세션에서 로그인된 사용자의 ID를 가져옵니다.
        String MId = (String) session.getAttribute("loginId");
        // 서비스 계층을 호출하여 히스토리를 저장합니다.
        hsvc.saveHistory(MId, pids);
        // 히스토리 페이지로 리디렉션합니다.
        return "redirect:/history?mid=" + MId;
    }
}
