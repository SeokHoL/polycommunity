package kr.ac.kopo.polycommunity.entity;

import jakarta.persistence.*;
import kr.ac.kopo.polycommunity.dto.CustomerBoardDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "customer_board")
public class CustomerBoardEntity extends BaseEntity {

    @Id //pk컬럼지정. 필수!
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long id;

    @Column(length = 20, nullable = false) //nullable은 null일수있다. nullable = false은 not null 이란 의미.
    private String boardWriter;

    @Column //이렇게 안정해주면 크기 255, null 가능 으로 됨.
    private String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    @Column(updatable = false)
    private LocalDateTime boardCreatedTime;

    @Column
    private LocalDateTime boardUpdatedTime;

    @Column
    private int fileAttached; //1 or 0

    @OneToMany(mappedBy = "customerBoardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CustomerCommentEntity> commentEntityList = new ArrayList<>();

    public static CustomerBoardEntity toSaveEntity(CustomerBoardDTO boardDTO) {
        CustomerBoardEntity boardEntity = new CustomerBoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter()); // 기본값 대신 실제 작성자를 설정
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardCreatedTime(boardDTO.getBoardCreatedTime());
        boardEntity.setBoardHits(0);
        return boardEntity;
    }


    public static CustomerBoardEntity toUpdateEntity(CustomerBoardDTO boardDTO) {
        CustomerBoardEntity boardEntity = new CustomerBoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        boardEntity.setBoardCreatedTime(boardDTO.getBoardCreatedTime());
        boardEntity.setBoardUpdatedTime(boardDTO.getBoardUpdatedTime());
        return boardEntity;
    }

    public static CustomerBoardEntity toSaveFileEntity(CustomerBoardEntity boardDTO) {
        CustomerBoardEntity boardEntity = new CustomerBoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        return boardEntity;
    }
}
