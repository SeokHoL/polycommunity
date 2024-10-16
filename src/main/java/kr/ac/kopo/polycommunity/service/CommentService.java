package kr.ac.kopo.polycommunity.service;

import kr.ac.kopo.polycommunity.dto.CommentDTO;
import kr.ac.kopo.polycommunity.entity.BoardEntity;
import kr.ac.kopo.polycommunity.entity.CommentEntity;
import kr.ac.kopo.polycommunity.repository.BoardRepository;
import kr.ac.kopo.polycommunity.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentReopsitory;
    private final BoardRepository boardRepository;

    public Long save(CommentDTO commentDTO) {
        //부모엔티티(BoardEntity) 조회
        Optional<BoardEntity> optionalBoardEntity =boardRepository.findById(commentDTO.getBoardId());
        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity =optionalBoardEntity.get();
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO,boardEntity );
            return commentReopsitory.save(commentEntity).getId();
        }else {
            return null;
        }

    }

    public List<CommentDTO> findAll(Long boardId) {

        //select * from comment_table where board_id=? order by id desc;
        BoardEntity boardEntity =boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList=commentReopsitory.findAllByBoardEntityOrderByIdDesc(boardEntity);
        //EntityList -> DTOList 로 변환
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for(CommentEntity commentEntity: commentEntityList){
            CommentDTO commentDTO= CommentDTO.toCommentDTO(commentEntity,boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;

    }
}
