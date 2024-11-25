package com.icia.devhub.Service;

import com.icia.devhub.dao.CommentRepository;
import com.icia.devhub.dto.Board.CommentDTO;
import com.icia.devhub.dto.Board.CommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository crepo;

    public List<CommentDTO> commentList(int CBNum) {
        List<CommentDTO> dtoList = new ArrayList<>();
        List<CommentEntity> entityList = crepo.findAllByBoard_BNum(CBNum);

        for(CommentEntity entity : entityList) {
            dtoList.add(CommentDTO.toDTO(entity));
        }

        return dtoList;
    }

    public void commentWrite(CommentDTO comment) {
        crepo.save(CommentEntity.toEntity(comment));
    }

    public void commentDelete(CommentDTO comment) {
        crepo.deleteById(CommentEntity.toEntity(comment).getCNum());
    }
}