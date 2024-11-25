package com.icia.devhub.Service;

import com.icia.devhub.dto.Order.ProductDTO;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender; // 이메일 전송을 위한 JavaMailSender 객체


    /**
     * 구매 내역을 이메일로 전송하는 메서드입니다.
     *
     * @param fromAddress   수신자 이메일 주소
     * @param product 구매한 제품 정보
     */
    public void sendPurchaseDetails(String fromAddress, HttpSession session, ProductDTO product) {
        try {
            // MimeMessage 객체 생성
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true, "UTF-8");


            // 이메일 본문 작성
            String message = "<div style=\"background-color: #333; color: #FFF; width: calc(100% / 3); margin: 0 auto; padding: 32px; border-radius: 16px; box-shadow: 0 8px 16px rgba(0,0,0,0.2); text-align: center;\">" +
                    "<h2 style=\"color: #FFD700; font-size: 36px; text-align: left;\">구매해주셔서 감사합니다.</h2>" +
                    "<h3 style=\"color: #FFD700; font-size: 24px; text-align: left;\">Thank you for your purchase!</h3>" +
                    "<p style=\"color: #FFF; font-size: 18px; text-align: left;\">구매 내역은 다음과 같습니다:</p>" +
                    "<p style=\"color: #FFD700; font-size: 18px; text-align: left;\">Order ID: " + product.getPId() + "<br>" +
                    "Product Name: " + product.getPName() + "<br>" +
                    "Quantity: 1<br>" +
                    "Price: " + product.getPPrice() + "</p>" +
                    "<img src=\"http://localhost:9091/images/robot.JPG\" alt=\"로봇 이미지\" style=\"width: 100%; height: auto; margin-top: 40px;\">" +
                    "<br><br>" +
                    "<p style=\"color: #FFF; font-size: 10px; text-align: right;\">자세한 문의 사항은 010-4135-4158로 문의바랍니다.</p>" +
                    "<p style=\"color: #FFF; font-size: 10px; text-align: right;\">For more information, please contact 010-4135-4158.</p>" +
                    "</div>";

            // 세션에서 로그인한 사용자의 이메일 주소를 가져옴
            String toEmail = (String) session.getAttribute("loginMEmail");
            if (toEmail == null) {
                throw new RuntimeException("로그인한 사용자의 이메일 주소를 찾을 수 없습니다.");
            }

            // 이메일 설정
            helper.setFrom(fromAddress); // 발신자 주소 설정
            helper.setTo(toEmail); // 수신자 주소 설정
            helper.setSubject("구매 내역서"); // 이메일 제목 설정
            helper.setText(message, true); // 이메일 본문 설정 (HTML 형식)

            // 이메일 전송
            mailSender.send(mail);
        } catch (Exception e) {
            throw new RuntimeException("이메일 전송 실패", e);
        }
    }
}
