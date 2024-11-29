package kr.ac.kopo.polycommunity.entity;

import jakarta.persistence.*;
import kr.ac.kopo.polycommunity.dto.CustomerCommentDTO;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "customer_comment_table")
public class CustomerCommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String commentWriter;

    @Column
    private String commentContents;

    /* Board:Comment = 1:N */
    @ManyToOne(fetch = FetchType.LAZY) //댓글이 기준
    @JoinColumn(name = "customer_id")
    private CustomerBoardEntity customerBoardEntity;

    public static CustomerCommentEntity toSaveEntity(CustomerCommentDTO commentDTO, CustomerBoardEntity boardEntity) {
        CustomerCommentEntity commentEntity = new CustomerCommentEntity();
        commentEntity.setCommentWriter(commentDTO.getCommentWriter());
        commentEntity.setCommentContents(commentDTO.getCommentContents());
        commentEntity.setCustomerBoardEntity(boardEntity);
        return commentEntity;
    }
}
