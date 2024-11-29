package kr.ac.kopo.polycommunity.controller;

import kr.ac.kopo.polycommunity.dto.CommentDTO;
import kr.ac.kopo.polycommunity.dto.CustomerCommentDTO;
import kr.ac.kopo.polycommunity.service.CommentService;
import kr.ac.kopo.polycommunity.service.CustomerServiceCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customerservicecomment")
public class CustomerServiceCommentController {

    private final CustomerServiceCommentService commentService;

    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CustomerCommentDTO commentDTO) {
        System.out.println("commentDTO= " + commentDTO);

        Long saveResult = commentService.save(commentDTO);

        if (saveResult != null) {
            //작성 성공하면 댓글목록을 가져와서 리턴
            //댓글목록: 해당 게시글의 댓글 전체
            List<CustomerCommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
            return new ResponseEntity(commentDTOList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("해당 게시글이 존재하지 않습니다.", HttpStatus.NOT_FOUND);
        }
    }
}
