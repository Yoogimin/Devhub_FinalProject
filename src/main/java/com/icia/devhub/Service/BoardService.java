package com.icia.devhub.Service;

import com.icia.devhub.dao.CodeRepository;
import com.icia.devhub.dao.BoardRepository;
import com.icia.devhub.dto.Board.BoardDTO;
import com.icia.devhub.dto.Board.BoardEntity;
import com.icia.devhub.dto.Board.CodeDTO;
import com.icia.devhub.dto.Board.CodeEntity;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository brepo;
    private final CodeRepository bcrepo;
    private ModelAndView mav;
    Path path = Paths.get(System.getProperty("user.dir"), "/src/main/resources/static/upload");
    private final HttpSession session;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    public String bWrite(BoardDTO board) {
        mav = new ModelAndView();

        String loginId = (String) session.getAttribute("loginId");
        board.setBWriter(loginId);

        //파일 가져오기
        MultipartFile BFile = board.getBFile();
        String saveFile = "";

        if(BFile != null) {
            String uuid = UUID.randomUUID().toString().substring(0, 8);
            String oFileName = BFile.getOriginalFilename();
            String BFileName = uuid + "_" + oFileName;
            board.setBFileName(BFileName);

            saveFile = path + "\\" + BFileName;
        } else {
            board.setBFileName("default.jpg");
        }

        BoardEntity entity;
        try {
            entity = BoardEntity.toEntity(board);
            brepo.save(entity);

            if(BFile != null) {
                BFile.transferTo(new File(saveFile));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return String.valueOf(BoardDTO.toDTO(entity).getBNum());
    }

    public void insertWrite(CodeDTO code) {
        bcrepo.save(CodeEntity.toEntity(code));
    }

    public List<BoardDTO> boardList() {
        List<BoardEntity> entityList = brepo.findAllByOrderByBNumDesc();
        List<BoardDTO> dtoList = new ArrayList<>();

        for(BoardEntity entity : entityList) {
            dtoList.add(BoardDTO.toDTO(entity));
        }

        return dtoList;
    }

    public List<BoardDTO> searchBList(String category, String keyword) {
        List<BoardEntity> entityList = new ArrayList<>();
        List<BoardDTO> dtoList = new ArrayList<>();

        if(category.equals("BWriter")) {
            entityList = brepo.findByMember_MIdContainingOrderByBNumDesc(keyword);
        } else if(category.equals("BTitle")) {
            entityList = brepo.findByBTitleContainingOrderByBNumDesc(keyword);
        } else if(category.equals("BContent")) {
            entityList = brepo.findByBContentContainingOrderByBNumDesc(keyword);
        }

        for(BoardEntity entity : entityList) {
            dtoList.add(BoardDTO.toDTO(entity));
        }

        return dtoList;
    }

    public ModelAndView bView(int BNum) {
        mav = new ModelAndView();

        String loginId = (String) session.getAttribute("loginId");
        if(loginId == null) {
            loginId = "Guest";
        }

        Cookie[] cookies = request.getCookies();
        Cookie viewCookie = null;

        //쿠키가 존재한다면
        if(cookies != null && cookies.length > 0) {

            //쿠키만큼 반복문을 실행
            for(Cookie cookie : cookies) {

                //쿠키의 이름이 ex)cookie_Guest_1과 같다면
                if(cookie.getName().equals("cookie_" + loginId + "_" + BNum)) {
                    //viewCookie에 cookie_Guest_1를 담는다.
                    viewCookie = cookie;
                }
            }
        }

        if(viewCookie == null) {
            Cookie newCookie = new Cookie("cookie_" + loginId + "_" + BNum, "cookie_" + loginId + "_" + BNum);
            newCookie.setMaxAge(60 * 60 * 1);       //초 * 분 * 시
            response.addCookie(newCookie);
            brepo.increaseBHit(BNum);
        }

        Optional<BoardEntity> entity = brepo.findById(BNum);
        if(entity.isPresent()) {
            BoardDTO dto = BoardDTO.toDTO(entity.get());
            mav.addObject("view", dto);

            List<CodeEntity> codeEntitys = bcrepo.findByBoard_BNumOrderByDTNumDesc(BNum);
            List<CodeDTO> codeDTO = new ArrayList<>();
            for(CodeEntity codeEntity : codeEntitys) {
                codeDTO.add(CodeDTO.toDTO(codeEntity));
            }

            mav.addObject("code", codeDTO);
        }

        mav.setViewName("board/view");

        return mav;
    }

    public ModelAndView bDelete(int BNum) {
        mav = new ModelAndView();
        Optional<BoardEntity> entity = brepo.findById(BNum);

        if(entity.isPresent()) {
            if(BoardDTO.toDTO(entity.get()).getBFileName() != null) {
                File delFile = new File(path + "/" + BoardDTO.toDTO(entity.get()).getBFileName());

                if(delFile.exists()) {
                    delFile.delete();
                }
            }
            for(CodeEntity c : bcrepo.findByBoard_BNum(BNum)) {
                bcrepo.deleteById(CodeDTO.toDTO(c).getDTNum());
            }
            brepo.deleteById(BoardDTO.toDTO(entity.get()).getBNum());
            mav.setViewName("redirect:/list");
        } else {
            mav.setViewName("index");
        }

        return mav;
    }

    public List<CodeDTO> codeCheck(int DDNum) {
        List<CodeDTO> dto = new ArrayList<>();
        List<CodeEntity> entityList = bcrepo.findByBoard_BNumOrderByDTNumDesc(DDNum);

        for(CodeEntity entity : entityList) {
            dto.add(CodeDTO.toDTO(entity));
        }

        return dto;
    }

    public ModelAndView modifyForm(int BNum) {
        mav = new ModelAndView();

        Optional<BoardEntity> board = brepo.findById(BNum);
        if(board.isPresent()) {
            mav.addObject("board", BoardDTO.toDTO(board.get()));
            mav.setViewName("board/modify");
        }

        return mav;
    }

    public void bModify(BoardDTO board) {
        MultipartFile BFile = board.getBFile();
        String saveFile = "";

        if(board.getBFileName() != null) {
            File delFile = new File(path + "/" + board.getBFileName());

            if(delFile.exists()) {
                delFile.delete();
            }
        }

        if(!BFile.isEmpty()) {
            String uuid = UUID.randomUUID().toString().substring(0, 8);
            String oFileName = BFile.getOriginalFilename();
            String BFileName = uuid + "_" + oFileName;
            board.setBFileName(BFileName);

            saveFile = path + "\\" + BFileName;
        }

        try {
            BFile.transferTo(new File(saveFile));
            brepo.save(BoardEntity.toEntity(board));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<CodeEntity> codeList = bcrepo.findByBoard_BNumOrderByDTNumDesc(board.getBNum());

        for(int i=0; i<codeList.size(); i++) {
            bcrepo.deleteById(codeList.get(i).getDTNum());
        }
    }
}