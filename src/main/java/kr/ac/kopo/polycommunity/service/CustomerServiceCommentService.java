package kr.ac.kopo.polycommunity.service;

import kr.ac.kopo.polycommunity.dto.CustomerCommentDTO;
import kr.ac.kopo.polycommunity.entity.CustomerBoardEntity;
import kr.ac.kopo.polycommunity.entity.CustomerCommentEntity;
import kr.ac.kopo.polycommunity.repository.CustomerServiceBoardRepository;
import kr.ac.kopo.polycommunity.repository.CustomerSeviceCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceCommentService {
    private final CustomerSeviceCommentRepository commentRepository;
    private final CustomerServiceBoardRepository boardRepository;

    public Long save(CustomerCommentDTO commentDTO) {
        //부모엔티티(CustomerBoardEntity) 조회
        Optional<CustomerBoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if (optionalBoardEntity.isPresent()) {
            CustomerBoardEntity boardEntity = optionalBoardEntity.get();
            CustomerCommentEntity commentEntity = CustomerCommentEntity.toSaveEntity(commentDTO, boardEntity);
            return commentRepository.save(commentEntity).getId();
        } else {
            return null;
        }
    }

    public List<CustomerCommentDTO> findAll(Long boardId) {
        //select * from comment_table where board_id=? order by id desc;
        CustomerBoardEntity boardEntity = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("Board not found"));
        List<CustomerCommentEntity> commentEntityList = commentRepository.findAllBycustomerBoardEntityOrderByIdDesc(boardEntity);
        //EntityList -> DTOList 로 변환
        List<CustomerCommentDTO> commentDTOList = new ArrayList<>();
        for (CustomerCommentEntity commentEntity : commentEntityList) {
            CustomerCommentDTO commentDTO = CustomerCommentDTO.toCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
