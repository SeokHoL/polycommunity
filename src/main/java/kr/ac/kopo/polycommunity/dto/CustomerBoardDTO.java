package kr.ac.kopo.polycommunity.dto;

import kr.ac.kopo.polycommunity.entity.BoardEntity;
import kr.ac.kopo.polycommunity.entity.CustomerBoardEntity;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor //기본생성자 를 만들어줌
@AllArgsConstructor //모든 필드를 매개변수로 하는 생성자를 만들어줌
public class CustomerBoardDTO {

    private Long id;
    private  String boardWriter;
    private String boardPass;
    private String boardTitle;
    private String boardContents;
    private int boardHits; //조회수

    private LocalDateTime boardCreatedTime;
    private LocalDateTime boardUpdatedTime;

    private List<MultipartFile> boardFile; // save.html -> Controller로 넘어올때 파일을 담는 용도
    private  List<String> orginalFileName; // 원본 파일 이름
    private  List<String> storedFileName;  // 서버 저장용 파일 이름
//    private  int fileAttached; //파일 첨부 여부(첨부 1, 미첨부 0)


    public CustomerBoardDTO(Long id, String boardWriter, String boardTitle, int boardHits, LocalDateTime boardCreatedTime) {
        this.id = id;
        this.boardWriter = boardWriter;
        this.boardTitle = boardTitle;
        this.boardHits = boardHits;
        this.boardCreatedTime = boardCreatedTime;
    }

    public static CustomerBoardDTO toBoardDTO(CustomerBoardEntity boardEntity){
        CustomerBoardDTO boardDTO = new CustomerBoardDTO();
        boardDTO.setId(boardEntity.getId());
        boardDTO.setBoardWriter(boardEntity.getBoardWriter());
        boardDTO.setBoardPass(boardEntity.getBoardPass());
        boardDTO.setBoardTitle(boardEntity.getBoardTitle());
        boardDTO.setBoardContents(boardEntity.getBoardContents());
        boardDTO.setBoardHits(boardEntity.getBoardHits());
        boardDTO.setBoardCreatedTime(boardEntity.getCreatedTime());
        boardDTO.setBoardUpdatedTime(boardEntity.getUpdatedTime());
//        if(boardEntity.getFileAttached() == 0){
//            boardDTO.setFileAttached(boardEntity.getFileAttached()); //0
//        }else {
//            List<String> originalFileNameList =new ArrayList<>();
//            List<String> storedFileNameList = new ArrayList<>();
//            boardDTO.setFileAttached(boardEntity.getFileAttached()); //1
//            for (BoardFileEntity boardFileEntity: boardEntity.getBoardFileEntityList()){
//                originalFileNameList.add(boardFileEntity.getOriginalFileName());
//                storedFileNameList.add(boardFileEntity.getStoredFileName());
//            }
//            boardDTO.setOrginalFileName(originalFileNameList);
//            boardDTO.setStoredFileName(storedFileNameList);


        return boardDTO;

    }
}
