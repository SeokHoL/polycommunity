package kr.ac.kopo.polycommunity.entity;

import jakarta.persistence.*;
import kr.ac.kopo.polycommunity.dto.BoardDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "board_table")
public class BoardEntity extends BaseEntity{
    @Id //pk컬럼지정. 필수!
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long id;

    @Column(length = 20, nullable = false) //nullable은 null일수있다. nullable = false은 not null 이란 의미.
    private String boardWriter;

    @Column //이렇게 안정해주면 크기 255, null 가능 으로 됨.
    private  String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private  String boardContents;

    @Column
    private  int boardHits;

    @Column(updatable = false)
    private LocalDateTime boardCreatedTime;
    @Column
    private LocalDateTime boardUpdatedTime;

    @Column
    private  int fileAttached; //1 or 0

    @OneToMany(mappedBy ="boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =FetchType.LAZY)
    private List<BoardFileEntity> boardFileEntityList = new ArrayList<>();

    @OneToMany(mappedBy ="boardEntity", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch =FetchType.LAZY)
    private List<CommentEntity> commentEntityList = new ArrayList<>();


    public static BoardEntity toSaveEntity(BoardDTO boardDTO){ //변환에서는 static 을 씀. 이러면 그냥  메소드명으로 사용가능.

        BoardEntity boardEntity = new BoardEntity();
//        boardEntity.setId(boardEntity.getId());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter()); //boardDTO.getBoardWriter() 를 setBoardWriter 여기에 넣는다. 옮겨 담는다.
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardCreatedTime(boardDTO.getBoardCreatedTime());
        boardEntity.setBoardHits(0);
//        boardEntity.setFileAttached(0); //파일이 없음.
        return boardEntity;
    }

    public static BoardEntity toUpdateEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter()); //boardDTO.getBoardWriter() 를 setBoardWriter 여기에 넣는다. 옮겨 담는다.
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
        boardEntity.setBoardCreatedTime(boardDTO.getBoardCreatedTime());
        boardEntity.setBoardUpdatedTime(boardDTO.getBoardUpdatedTime());
        return boardEntity;
    }


    public static BoardEntity toSaveFileEntity(BoardDTO boardDTO) {
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setBoardWriter(boardDTO.getBoardWriter()); //boardDTO.getBoardWriter() 를 setBoardWriter 여기에 넣는다. 옮겨 담는다.
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(boardDTO.getBoardHits());
//        boardEntity.setFileAttached(1); //파일 있음.
        return boardEntity;

    }
}
