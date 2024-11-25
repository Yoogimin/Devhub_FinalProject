package com.icia.devhub.Controller;

import com.icia.devhub.Service.MemberService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PayPointController {

    private final MemberService msvc;

    private final HttpSession session;

    @GetMapping("/pay")
    public String signup() {
        return "pointPayment";
    }

}