package com.icia.devhub.Controller;

import com.icia.devhub.Service.OrderService;
import com.icia.devhub.dto.Order.ProductDTO;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService osvc;

    @PostMapping("/charge")
    public ResponseEntity<Map<String, Object>> chargePoints(HttpSession session, @RequestBody Map<String, Object> payload) {
        String MId = (String) session.getAttribute("loginId"); // 세션에서 로그인 ID 가져오기
        Map<String, Object> response = new HashMap<>();
        if (MId != null) {
            int MPoint = (int) payload.get("MPoint"); // 충전할 포인트
            String fromAddress = "hajincheol123@gmail.com"; // 이메일 주소
            Map<String, Object> orderMap = (Map<String, Object>) payload.get("order"); // 주문 정보

            // ProductDTO 객체 생성 및 데이터 설정
            ProductDTO order = new ProductDTO();
            order.setPName((String) orderMap.get("PName"));
            order.setPPrice((int) orderMap.get("PPrice"));
            order.setPCategory((String) orderMap.get("PCategory"));
            order.setPExplain((String) orderMap.get("PExplain"));

            // 포인트 충전 및 이메일 전송
            int points = osvc.chargePoints(MId, MPoint, session, order, fromAddress);
            if (points > 0) {
                response.put("message", "Points charged and email sent.");
                response.put("points", points);
                response.put("redirectUrl", "/"); // 리디렉션 URL 포함
                return ResponseEntity.ok(response); // 성공 응답
            } else {
                response.put("message", "Failed to charge points.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 실패 응답
            }
        } else {
            response.put("message", "Invalid session or user not logged in.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 유효하지 않은 세션 응답
        }
    }


    @PostMapping("/deductPoints")
    public ResponseEntity<Integer> deductPoints(HttpSession session, @RequestParam("MPoint") int MPoint) {
        String MId = (String) session.getAttribute("loginId"); // 세션에서 로그인 ID 가져오기

        if (MId != null) {
            int points = osvc.deductPoints(MId, MPoint); // 포인트 차감
            return ResponseEntity.ok(points); // 성공 응답
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(0); // 인증되지 않은 사용자 응답
        }
    }

    @GetMapping("/getPoints")
    public ResponseEntity<Integer> getPoints(HttpSession session) {
        String MId = (String) session.getAttribute("loginId");
        if (MId != null) {
            int points = osvc.getMemberPoints(MId);
            return ResponseEntity.ok(points);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(0);
        }
    }

    @Transactional
    @PostMapping("/getproduct")
    public ResponseEntity<Map<String, Object>> getproduct(HttpSession session, @RequestBody Map<String, Object> payload) {
        String loginId = (String) session.getAttribute("loginId"); // 세션에서 로그인 ID 가져오기

        Map<String, Object> response = new HashMap<>();
        if (loginId != null) {
            String fromAddress = "hajincheol123@gmail.com"; // 이메일 주소
            Map<String, Object> orderMap = (Map<String, Object>) payload.get("order");

            // ProductDTO 객체 생성 및 데이터 설정
            ProductDTO order = new ProductDTO();
            order.setPName((String) orderMap.get("PName"));
            order.setPPrice((int) orderMap.get("PPrice"));
            order.setPCategory((String) orderMap.get("PCategory"));
            order.setPExplain((String) orderMap.get("PExplain"));

            // PId 추가
            Integer productId = (Integer) orderMap.get("PId");

            if (productId == null) {
                response.put("message", "Product ID is missing");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            //이메일 전송
            String UI = osvc.insertProduct(loginId, session, order, fromAddress);
            if (UI != null) {
                response.put("message", "email sent.");
                response.put("UI", UI);
                response.put("PId", productId); // PId 추가
                return ResponseEntity.ok(response); // 성공 응답
            } else {
                response.put("message", "Failed to purchase product.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response); // 실패 응답
            }
        } else {
            response.put("message", "Invalid session or user not logged in.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); // 유효하지 않은 세션 응답
        }
    }
}