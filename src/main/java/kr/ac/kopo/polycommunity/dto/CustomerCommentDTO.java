package kr.ac.kopo.polycommunity.dto;

import kr.ac.kopo.polycommunity.entity.CommentEntity;
import kr.ac.kopo.polycommunity.entity.CustomerCommentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
public class CustomerCommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Long boardId;
    private LocalDateTime commentCreatedTime;

    public static CustomerCommentDTO toCommentDTO(CustomerCommentEntity commentEntity, Long boardId){
        CustomerCommentDTO commentDTO =new CustomerCommentDTO();
        commentDTO.setId(commentEntity.getId());
        commentDTO.setCommentWriter(commentEntity.getCommentWriter());
        commentDTO.setCommentContents(commentEntity.getCommentContents());
        commentDTO.setCommentCreatedTime(commentEntity.getCreatedTime());
        commentDTO.setBoardId(boardId);
        return commentDTO;
    }
    public String getFormattedCreatedTime() {
        if (this.commentCreatedTime == null) {
            return "";
        }
        return this.commentCreatedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

}

