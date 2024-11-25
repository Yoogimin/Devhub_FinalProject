package com.icia.devhub.Controller;

import com.icia.devhub.dto.Member.LoginDTO;
import com.icia.devhub.Service.MemberService;
import com.icia.devhub.dto.Member.LoginEntity;
import com.icia.devhub.dto.Member.MemberDTO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller // 해당 클래스가 Spring MVC의 컨트롤러임을 나타냅니다.
@RequiredArgsConstructor // Lombok을 사용하여 final 필드에 대한 생성자를 자동으로 생성합니다.
public class MemberController {
    private final MemberService msvc; // MemberService를 주입받습니다.

    private final HttpSession session; // HttpSession 객체를 주입받습니다.

    @GetMapping("/") // 기본 루트 URL 요청을 처리합니다.
    public String startPage() {
        return "index"; // index.html 뷰를 반환합니다.
    }

    @GetMapping("/index") // "/index" URL에 GET 요청이 오면 실행되는 메서드입니다.
    public String index() {
        return "index"; // "index" 뷰를 반환합니다.
    }
    @GetMapping("/aiChat") // "/index" URL에 GET 요청이 오면 실행되는 메서드입니다.
    public String aiChat() {
        return "aiChat"; // "index" 뷰를 반환합니다.
    }

    @GetMapping("/Chat") // "/index" URL에 GET 요청이 오면 실행되는 메서드입니다.
    public String Chat() {
        return "body/chat"; // "index" 뷰를 반환합니다.
    }
    @GetMapping("/login") // "/login" URL에 GET 요청이 오면 실행되는 메서드입니다.
    public String login() {
        return "/member/login"; // "member/login" 뷰를 반환합니다.
    }

    @GetMapping("/signup") // "/signup" URL에 GET 요청이 오면 실행되는 메서드입니다.
    public String signup() {
        return "/member/signup"; // "member/signup" 뷰를 반환합니다.
    }

    @GetMapping("/reset") // "/reset" URL에 GET 요청이 오면 실행되는 메서드입니다.
    public String reset() {
        return "/member/reset"; // "member/reset" 뷰를 반환합니다.
    }

    @GetMapping("/mLogout") // "/mLogout" URL에 GET 요청이 오면 실행되는 메서드입니다.
    public String mLogout() {
        session.invalidate(); // 세션을 무효화합니다.
        return "redirect:/index"; // "index"로 리다이렉트합니다.
    }

    @GetMapping("/about") // "/about" URL에 GET 요청이 오면 실행되는 메서드입니다.
    public String about() {
        return "about"; // "about" 뷰를 반환합니다.
    }

    @GetMapping("/contact") // "/contact" URL에 GET 요청이 오면 실행되는 메서드입니다.
    public String contact() {
        return "contact"; // "contact" 뷰를 반환합니다.
    }

    @GetMapping("/coding") // "/coding" URL에 GET 요청이 오면 실행되는 메서드입니다.
    public String coding() {
        return "coding"; // "coding" 뷰를 반환합니다.
    }

    @PostMapping("/mJoin") // "/mJoin" URL에 POST 요청이 오면 실행되는 메서드입니다.
    public ModelAndView mJoin(@ModelAttribute MemberDTO member) {
        return msvc.mJoin(member); // MemberService의 mJoin 메서드를 호출하고 그 결과를 반환합니다.
    }

    @PostMapping("/mLogin")
    public ModelAndView mLogin(@ModelAttribute MemberDTO member) {
        return msvc.mLogin(member);
    }

    @PostMapping("/log")
    public @ResponseBody void mLogin(@ModelAttribute LoginDTO dto) {
        msvc.log(dto);
    }

    @RestController // 해당 클래스가 RESTful 컨트롤러임을 나타냅니다.
    @RequestMapping("/api") // "/api" URL에 대한 요청을 처리합니다.
    public static class SessionController {
        private final HttpSession session; // HttpSession 객체를 주입받습니다.

        @Autowired // 세션을 주입받기 위해 사용됩니다.
        public SessionController(HttpSession session) {
            this.session = session; // 주입된 세션을 필드에 할당합니다.
        }

        @GetMapping("/checkSession") // "/api/checkSession" URL에 GET 요청이 오면 실행되는 메서드입니다.
        public ResponseEntity<String> checkSession() {
            String loginId = (String) session.getAttribute("loginId"); // 세션에서 loginId 속성을 가져옵니다.

            if (loginId != null) { // loginId가 존재하면
                return ResponseEntity.ok("success"); // "success"를 반환합니다.
            } else { // loginId가 존재하지 않으면
                return ResponseEntity.status(401).body("fail"); // "fail"을 반환합니다.
            }
        }
    }

    @GetMapping("/mView/{MId}") // "/mView/{MId}" URL에 GET 요청이 오면 실행되는 메서드입니다.
    public ModelAndView mView(@PathVariable("MId") String MId) {
        return msvc.mView(MId); // MemberService의 mView 메서드를 호출하고 그 결과를 반환합니다.
    }

    @GetMapping("/modiForm/{MId}") // "/modiForm/{MId}" URL에 GET 요청이 오면 실행되는 메서드입니다.
    public ModelAndView modiForm(@PathVariable("MId") String MId) {
        return msvc.modiForm(MId); // MemberService의 modiForm 메서드를 호출하고 그 결과를 반환합니다.
    }

    @PostMapping("/mModify") // "/mModify" URL에 POST 요청이 오면 실행되는 메서드입니다.
    public ModelAndView mModify(@ModelAttribute MemberDTO member) {
        return msvc.mModify(member); // MemberService의 mModify 메서드를 호출하고 그 결과를 반환합니다.
    }

    @GetMapping("/deleteForm/{MId}") // "/deleteForm/{MId}" URL에 GET 요청이 오면 실행되는 메서드입니다.
    public ModelAndView deleteForm(@PathVariable("MId") String MId) {
        return msvc.deleteForm(MId); // MemberService의 deleteForm 메서드를 호출하고 그 결과를 반환합니다.
    }

    @PostMapping("/mDelete") // "/mDelete" URL에 POST 요청이 오면 실행되는 메서드입니다.
    public ModelAndView mDelete(@ModelAttribute MemberDTO member) {
        return msvc.mDelete(member); // MemberService의 mDelete 메서드를 호출하고 그 결과를 반환합니다.
    }

    @GetMapping("/logHistory")
    @ResponseBody
    public List<LoginDTO> getLoginHistory(HttpSession session) {
        String userId = (String) session.getAttribute("loginId");
        return msvc.getLoginHistoryByUserId(userId);
    }

}
