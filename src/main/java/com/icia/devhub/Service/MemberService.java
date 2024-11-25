package com.icia.devhub.Service;

import com.icia.devhub.dao.*;
import com.icia.devhub.dto.Board.BoardDTO;
import com.icia.devhub.dto.Board.BoardEntity;
import com.icia.devhub.dto.Board.CodeDTO;
import com.icia.devhub.dto.Board.CodeEntity;
import com.icia.devhub.dto.Member.LoginDTO;
import com.icia.devhub.dto.Member.LoginEntity;
import com.icia.devhub.dto.Member.MemberEntity;
import com.icia.devhub.dto.Member.MemberDTO;
import com.icia.devhub.dto.Order.ProductDTO;
import com.icia.devhub.dto.Order.ProductEntity;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service // 해당 클래스가 서비스 레이어임을 나타냅니다.
@RequiredArgsConstructor // Lombok을 사용하여 final 필드에 대한 생성자를 자동으로 생성합니다.
public class MemberService {
    private final JavaMailSender mailSender; // JavaMailSender 객체를 주입받습니다.
    private final PasswordEncoder pwEnc; // PasswordEncoder 객체를 주입받습니다.
    private final HttpServletRequest request; // HttpServletRequest 객체를 주입받습니다.
    private final HttpSession session; // HttpSession 객체를 주입받습니다.
    private ModelAndView mav; // ModelAndView 객체를 선언합니다.
    Path path = Paths.get(System.getProperty("user.dir"), "/src/main/resources/static/profile"); // 파일 저장 경로를 설정합니다.

    private final MemberRepository mrepo; // MemberRepository 객체를 주입받습니다.
    private final LoginRepository lrepo; // LoginRepository 객체를 주입받습니다.
    private final BoardRepository brepo;
    private final CodeRepository bcrepo;
    private final CommentRepository crepo;
    private final CouponRepository cprepo;
    private final EventRepository erepo;
    private final CartRepository cartrepo;
    private final CategoryRepository cgrepo;
    private final HistoryRepository hrepo;
    private final PaymentRepository pmrepo;
    private final ProductRepository pdrepo;
    private final ProjectRepository pjrepo;
    private final ResumeRepository rrepo;
    private final TeamRepository trepo;

    // 아이디 중복 체크 메서드
    public String idCheck(String MId) {
        String result = "";
        Optional<MemberEntity> entity = mrepo.findById(MId); // 아이디로 회원을 조회합니다.
        if (entity.isPresent()) { // 회원이 존재하면
            result = "NO"; // "NO"를 반환합니다.
        } else { // 회원이 존재하지 않으면
            result = "OK"; // "OK"를 반환합니다.
        }
        return result; // 결과를 반환합니다.
    }

    // 이메일 인증 메서드
    public String emailCheck(String MEmail, String check) {
        String uuid = "";
        String numberType = "";

        if (check.equals("비밀번호")) { // 비밀번호 찾기일 경우
            numberType = "password"; // numberType을 "password"로 설정합니다.
        } else { // 인증번호 발송일 경우
            numberType = "authentication number"; // numberType을 "authentication number"로 설정합니다.
        }
        // 인증번호 생성
        uuid = UUID.randomUUID().toString().substring(0, 8); // UUID를 생성하여 인증번호로 사용합니다.

        // 이메일 발송
        MimeMessage mail = mailSender.createMimeMessage(); // MimeMessage 객체를 생성합니다.
        String message = "<div style=\"background-color: #333; color: #FFF; width: calc(100% / 3); margin: 0 auto; padding: 32px; border-radius: 16px; box-shadow: 0 8px 16px rgba(0,0,0,0.2); text-align: center;\">" + "<h2 style=\"color: #FFD700; font-size: 36px; text-align: left;\">안녕하세요. DevHub 입니다.</h2>" + "<h3 style=\"color: #FFD700; font-size: 24px; text-align: left;\">Hi. We are DevHub</h3>" + "<p style=\"color: #FFF; font-size: 18px; text-align: left;\">" + check + "는 <strong style=\"color: #FFD700;\"> " + uuid + "</strong> 입니다.</p>" + "<p style=\"color: #FFF; font-size: 18px; text-align: left;\">The " + numberType + " is <strong style=\"color: #FFD700;\"> " + uuid + "</strong>.</p>" + "<img src=\"http://localhost:9091/images/robot.JPG\" alt=\"로봇 이미지\" style=\"width: 100%; height: auto; margin-top: 40px;\">" + "<br>" + "<br>" + "<p style=\"color: #FFF; font-size: 10px; text-align: right;\">자세한 문의 사항은 010-4135-4158로 문의바랍니다.</p>" + "<p style=\"color: #FFF; font-size: 10px; text-align: right;\">For more information, please contact 010-4135-4158.</p>" + "</div>";

        try {
            mail.setSubject("인천일보 아카데미 인증번호"); // 이메일 제목을 설정합니다.
            mail.setText(message, "UTF-8", "html"); // 이메일 내용을 설정합니다.
            mail.addRecipient(Message.RecipientType.TO, new InternetAddress(MEmail)); // 수신자를 설정합니다.

            mailSender.send(mail); // 이메일을 발송합니다.
        } catch (Exception e) {
            throw new RuntimeException(e); // 예외 발생 시 런타임 예외를 던집니다.
        }
        return uuid; // 인증번호를 반환합니다.
    }

    // 회원가입 메서드
    public ModelAndView mJoin(MemberDTO member) {
        mav = new ModelAndView();

        // [1] 파일 업로드
        MultipartFile MProfile = member.getMProfile(); // 프로필 파일을 가져옵니다.
        if (!MProfile.isEmpty()) { // 파일이 비어있지 않으면
            String uuid = UUID.randomUUID().toString().substring(0, 8); // UUID를 생성합니다.
            String originalFilename = MProfile.getOriginalFilename(); // 원본 파일명을 가져옵니다.
            String MProfileName = uuid + "_" + originalFilename; // 저장할 파일명을 설정합니다.
            member.setMProfileName(MProfileName); // DTO에 파일명을 설정합니다.

            String savePath = path + "\\" + MProfileName; // 파일 저장 경로를 설정합니다.
            try {
                MProfile.transferTo(new File(savePath)); // 파일을 저장합니다.
            } catch (IOException e) {
                throw new RuntimeException(e); // 예외 발생 시 런타임 예외를 던집니다.
            }
        }

        // [2] 비밀번호 암호화
        member.setMPw(pwEnc.encode(member.getMPw())); // 비밀번호를 암호화하여 설정합니다.

        // [3] DTO -> Entity 변환 및 저장
        try {
            mrepo.save(MemberEntity.toEntity(member)); // DTO를 Entity로 변환하여 저장합니다.
        } catch (Exception e) {
            throw new RuntimeException(e); // 예외 발생 시 런타임 예외를 던집니다.
        }
        mav.setViewName("/member/login"); // 뷰 이름을 설정합니다.

        return mav; // ModelAndView를 반환합니다.
    }

    // 로그인 메서드
    public ModelAndView mLogin(MemberDTO member) {
        mav = new ModelAndView();

        // [1] MId 존재 여부 확인
        Optional<MemberEntity> entity = mrepo.findById(member.getMId()); // 아이디로 회원을 조회합니다.
        if (entity.isPresent()) { // 회원이 존재하면
            // [2] 해당 아이디에 대한 암호화된 비밀번호와 입력한 비밀번호가 일치하는지 확인
            if (pwEnc.matches(member.getMPw(), entity.get().getMPw())) { // 비밀번호가 일치하면
                MemberDTO login = MemberDTO.toDTO(entity.get()); // Entity를 DTO로 변환합니다.

                session.setAttribute("loginId", login.getMId()); // 세션에 로그인 아이디를 저장합니다.
                session.setAttribute("loginProfile", login.getMProfileName()); // 세션에 프로필 이름을 저장합니다.
                session.setAttribute("loginMEmail", login.getMEmail()); // 세션에 프로필 이름을 저장합니다.
                session.setAttribute("loginMPoint", login.getMPoint()); // 세션에 프로필 이름을 저장합니다.
                session.setAttribute("loginName", login.getMName());
                session.setAttribute("loggedInUser", true); // ;

            }
        }
        mav.setViewName("index"); // 뷰 이름을 설정합니다.

        return mav; // ModelAndView를 반환합니다.
    }

    // 회원 정보 수정 메서드
    public ModelAndView mModify(MemberDTO member) {
        mav = new ModelAndView();

        if(member.getMProfileName() != null){
            String delPath = path + "\\" + member.getMProfileName();

            File delFile = new File(delPath);
            if(delFile.exists()) {
                delFile.delete();
            }
        }

        // [1] 파일 업로드
        MultipartFile MProfile = member.getMProfile();

        if(!MProfile.isEmpty()) {
            String uuid = UUID.randomUUID().toString().substring(0,8);
            String originalFilename = MProfile.getOriginalFilename();
            String MProfileName = uuid + "_" + originalFilename;

            member.setMProfileName(MProfileName);

            String savePath = path + "\\" + MProfileName;

            try {
                MProfile.transferTo(new File(savePath));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        // [2] 비밀번호 암호화
        member.setMPw(pwEnc.encode(member.getMPw()));

        // [3] DTO → Entity 변환
        MemberEntity entity = MemberEntity.toEntity(member);

        try {
            mrepo.save(entity);
        } catch (Exception e){
            throw new RuntimeException(e);
        }

        mav.setViewName("redirect:/index");
        return mav;
    }

    // 회원 탈퇴 메서드
    public ModelAndView mDelete(MemberDTO member) {
        mav = new ModelAndView();

        // 파일 존재 시 삭제
        if (member.getMProfileName() != null) { // 프로필 파일명이 존재하면
            String delPath = path + "\\" + member.getMProfileName(); // 삭제할 파일 경로를 설정합니다.

            File delFile = new File(delPath); // 파일 객체를 생성합니다.
            if (delFile.exists()) { // 파일이 존재하면
                delFile.delete(); // 파일을 삭제합니다.
            }
        }

        List<ProductEntity> pentity = pdrepo.findByMember_MId(member.getMId());
        Optional<MemberEntity> mentity = mrepo.findById(member.getMId());
        if(mentity.isPresent()) {
            List<BoardEntity> bentity = brepo.findByMember_MId(member.getMId());
            for(BoardEntity b : bentity) {
                List<CodeEntity> centity = bcrepo.findByBoard_BNum(BoardDTO.toDTO(b).getBNum());
                for(CodeEntity c : centity) {
                    bcrepo.deleteById(CodeDTO.toDTO(c).getDTNum());
                }
            }
        }
        crepo.deleteByMember_MId(member.getMId());
        brepo.deleteByMember_MId(member.getMId());
        rrepo.deleteByMember_MId(member.getMId());
        pjrepo.deleteByMember_MId(member.getMId());
        trepo.deleteByMember_MId(member.getMId());
        pdrepo.deleteByMember_MId(member.getMId());
        pmrepo.deleteByMember_MId(member.getMId());
        hrepo.deleteByMember_MId(member.getMId());
        for(ProductEntity p : pentity) {
            cgrepo.deleteById(ProductDTO.toDTO(p).getPCId());
        }
        cartrepo.deleteByMember_MId(member.getMId());
        erepo.deleteByMember_MId(member.getMId());
        cprepo.deleteByMember_MId(member.getMId());
        lrepo.deleteByMember_MId(member.getMId());

        try {
            mrepo.deleteById(member.getMId()); // 아이디로 회원을 삭제합니다.
            session.invalidate(); // 세션을 무효화합니다.
        } catch (Exception e) {
            throw new RuntimeException(e); // 예외 발생 시 런타임 예외를 던집니다.
        }
        mav.setViewName("redirect:/index"); // 뷰 이름을 설정합니다.

        return mav; // ModelAndView를 반환합니다.
    }

    // 비밀번호 재설정 메서드
    public void resetPassword(MemberDTO member) {
        Optional<MemberEntity> entity = mrepo.findById(member.getMId()); // 아이디로 회원을 조회합니다.

        if (entity.isPresent()) { // 회원이 존재하면
            MemberDTO memberDTO = MemberDTO.toDTO(entity.get()); // Entity를 DTO로 변환합니다.

            // [1] 비밀번호 암호화
            memberDTO.setMPw(pwEnc.encode(member.getMPw())); // 비밀번호를 암호화하여 설정합니다.

            // [2] 비밀번호 재설정
            mrepo.save(MemberEntity.toEntity(memberDTO)); // DTO를 Entity로 변환하여 저장합니다.
        }
    }

    // 회원 목록 조회 메서드
    public List<MemberDTO> memberList() {
        List<MemberEntity> entity = new ArrayList<>(); // 회원 Entity 리스트를 선언합니다.
        List<MemberDTO> dto = new ArrayList<>(); // 회원 DTO 리스트를 선언합니다.

        entity = mrepo.findAll(); // 모든 회원을 조회합니다.
        for (int i = 0; i < entity.size(); i++) { // 조회된 회원을 반복하여
            dto.add(MemberDTO.toDTO(entity.get(i))); // Entity를 DTO로 변환하여 리스트에 추가합니다.
        }

        return dto; // 회원 DTO 리스트를 반환합니다.
    }

    // 회원정보 수정 페이지 이동 메서드
    public ModelAndView modiForm(String MId) {
        mav = new ModelAndView();

        // 회원 정보 상세보기 메서드에서 정보를 불러옵니다.
        MemberDTO member = (MemberDTO) mView(MId).getModel().get("view");

        mav.setViewName("member/modify"); // 뷰 이름을 설정합니다.
        mav.addObject("modify", member); // 회원 정보를 모델에 추가합니다.

        return mav; // ModelAndView를 반환합니다.
    }

    // 회원 삭제 페이지 열기 메서드
    public ModelAndView deleteForm(String MId) {
        mav = new ModelAndView();
        Optional<MemberEntity> entity = mrepo.findById(MId); // 아이디로 회원을 조회합니다.
        if (entity.isPresent()) { // 회원이 존재하면
            MemberDTO member = MemberDTO.toDTO(entity.get()); // Entity를 DTO로 변환합니다.
            mav.addObject("delete", member); // 회원 정보를 모델에 추가합니다.
            mav.setViewName("member/delete"); // 뷰 이름을 설정합니다.
        } else { // 회원이 존재하지 않으면
            mav.setViewName("redirect:/index"); // 뷰 이름을 설정합니다.
        }
        return mav; // ModelAndView를 반환합니다.
    }

    // 회원 정보 상세보기 메서드
    public ModelAndView mView(String MId) {
        mav = new ModelAndView();
        Optional<MemberEntity> entity = mrepo.findById(MId); // 아이디로 회원을 조회합니다.
        if (entity.isPresent()) { // 회원이 존재하면
            MemberDTO member = MemberDTO.toDTO(entity.get()); // Entity를 DTO로 변환합니다.
            mav.addObject("view", member); // 회원 정보를 모델에 추가합니다.
            mav.setViewName("member/view"); // 뷰 이름을 설정합니다.
        } else { // 회원이 존재하지 않으면
            mav.setViewName("redirect:/index"); // 뷰 이름을 설정합니다.
        }
        return mav; // ModelAndView를 반환합니다.
    }

    public void log(LoginDTO dto) {

        LoginEntity entity = LoginEntity.toEntity(dto);
        MemberEntity member = new MemberEntity();
        member.setMId((String)session.getAttribute("loginId"));
        entity.setMember(member);

        lrepo.save(entity);
    }

    public List<LoginDTO> getLoginHistoryByUserId(String userId) {

        List<LoginEntity> entityList = lrepo.findByMember_MIdOrderByLDateDesc(userId);
        List<LoginDTO> dtoList = new ArrayList<LoginDTO>();

        // entity 갯수만큼 반복문 실행 : entityList 목록 중 하나의 항목을 entity로 지정
        for (LoginEntity entity : entityList) {
            // entity를 dto로 변환하여 dtoList에 추가(add)
            dtoList.add(LoginDTO.toDTO(entity));
        }
        return dtoList;
    }

}