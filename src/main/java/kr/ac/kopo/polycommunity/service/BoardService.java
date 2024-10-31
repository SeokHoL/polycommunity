package kr.ac.kopo.polycommunity.service;

import jakarta.transaction.Transactional;
import kr.ac.kopo.polycommunity.dto.BoardDTO;
import kr.ac.kopo.polycommunity.entity.BoardEntity;


import kr.ac.kopo.polycommunity.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private  final BoardRepository boardRepository;



    public void save(BoardDTO boardDTO) throws IOException {
//        // 파일 첨부 여부에 따라 로직 분리
//        if(boardDTO.getBoardFile().isEmpty()){
//            //첨부 파일 없는경우.
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO);
        boardRepository.save(boardEntity);
//        }else{
//            //첨부 파일이 있는경우.
//            /*
//                1.DTO에 담긴 파일을 꺼냄
//                2.파일의 이름 가져옴
//                3.서버 저장용 이름을 만듬 //내사진.jpg =>8394949492_내사진.jpg
//                4.저장 경로 설정
//                5.해당 경로에 파일 저장
//                6.board_table에 해당 데이터 save 처리
//                7.board_file_table에 해당 데이터 save 처리
//            */
//            BoardEntity boardEntity =BoardEntity.toSaveFileEntity(boardDTO);
//            Long saveId =boardRepository.save(boardEntity).getId();
//            BoardEntity board =boardRepository.findById(saveId).get();
//            for(MultipartFile boardFile: boardDTO.getBoardFile()){
////                MultipartFile boardFile =boardDTO.getBoardFile(); //1번
//                String originalFilename = boardFile.getOriginalFilename(); //2번
//                String  storedFileName =System.currentTimeMillis() + "_" + originalFilename; //3번
//                String savePath = "C:/springboot_img/" + storedFileName; // 4번  C:/springboot_img/943094793749_내사진.jpg
//                //String savePath = "/Users/AISW-509-186/springboot_img/" + storedFileName; //C:/springboot_img/943094793749_내사진.jpg
//                boardFile.transferTo(new File(savePath)); //5번
//                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
//                boardFileRepository.save(boardFileEntity);
//            }
//        }


    }

    @Transactional
    public List<BoardDTO> findAll() {
        List<BoardEntity> boardEntityList=boardRepository.findAllByOrderByIdDesc();
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for(BoardEntity boardEntity: boardEntityList){
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    @Transactional //jpa에 query 같은거 작성하면 트랜잭션 어노테이션을 써줘야됨.
    public void updateHits(Long id) {
        boardRepository.updateHits(id);

    }
    @Transactional
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity =boardRepository.findById(id);
        if(optionalBoardEntity.isPresent()){
            BoardEntity boardEntity =optionalBoardEntity.get();
            BoardDTO boardDTO =BoardDTO.toBoardDTO(boardEntity);
            return  boardDTO;
        }else {
            return null;
        }

    }
    @Transactional
    public List<BoardDTO> searchBoards(String searchKeyword) {
        List<BoardEntity> boardEntityList = boardRepository.findByBoardTitleContaining(searchKeyword);
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }

    public BoardDTO update(BoardDTO boardDTO) {
        // 기존 엔티티를 조회
        BoardEntity existingEntity = boardRepository.findById(boardDTO.getId())
                .orElseThrow(() -> new RuntimeException("Board not found"));

        // 작성일을 기존 엔티티에서 가져와 설정
        boardDTO.setBoardCreatedTime(existingEntity.getCreatedTime());

        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId());
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page =pageable.getPageNumber() -1;
        int pageLimit = 5; //한 페이지에 보여줄 글 갯수
        //한 페이지당 3개씩 글을 보여주고 정렬 기준은 id기준으로 내림차순 정렬
        //page 위치에 있는 값은 0부터 시작
        Page<BoardEntity> boardEntities = boardRepository.findAll(PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC,"id")));
        //page 몇페이지를 볼거고, pageLimit 한페이에 몇개의 글을 볼거고,  Sort.by 어떻게 정렬할건지, id를 기준으로 정렬

        /*왜 -1을 해야 하나요?
        사용자 관점 (1부터 시작): 사용자나 프론트엔드에서 페이지를 요청할 때, 첫 번째 페이지를 1로 요청하는 것이 일반적입니다. 즉, 첫 번째 페이지는 1, 두 번째 페이지는 2 이런 식으로 요청합니다.

        Spring Data JPA 관점 (0부터 시작): 하지만 Spring Data JPA의 PageRequest.of 메서드는 페이지 번호를 0부터 시작하는 것으로 기대합니다. 즉, 첫 번째 페이지를 0으로, 두 번째 페이지를 1로 처리합니다.

        이 차이를 맞춰주기 위해, Pageable 객체에서 가져온 페이지 번호에서 1을 빼줘야 합니다. 이렇게 하면 사용자가 1페이지를 요청했을 때, 내부적으로는 0페이지를 요청하게 되어 정확한 페이지를 반환할 수 있게 됩니다. */

        //목록: id, writer, title, hits, createdTime
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(),board.getBoardWriter(),board.getBoardTitle(),board.getBoardHits(),board.getCreatedTime()));
        //board 는 boardEntities객체이고 이걸 하나씩 꺼내서 new BoardDTO()객체에 옮겨담음
        return boardDTOS;


    }
    //    public Page<BoardDTO> searchBoardsWithPaging(String keyword, Pageable pageable) {
//        int page = pageable.getPageNumber() - 1;
//        int pageLimit = 3;
//        Page<BoardEntity> boardEntities = boardRepository.findByBoardTitleContaining(
//                keyword,
//                PageRequest.of(page, pageLimit, Sort.by(Sort.Direction.DESC, "id"))
//        );
//        return boardEntities.map(board -> new BoardDTO(
//                board.getId(),
//                board.getBoardWriter(),
//                board.getBoardTitle(),
//                board.getBoardHits(),
//                board.getCreatedTime()
//        ));
//    }
    @Transactional
    public List<BoardDTO> searchBoardsByTitle(String searchKeyword) {
        List<BoardEntity> boardEntityList = boardRepository.findByBoardTitleContaining(searchKeyword);
        return convertToDTOList(boardEntityList);
    }

    @Transactional
    public List<BoardDTO> searchBoardsByWriter(String searchKeyword) {
        List<BoardEntity> boardEntityList = boardRepository.findByBoardWriterContaining(searchKeyword);
        return convertToDTOList(boardEntityList);
    }


    @Transactional
    public List<BoardDTO> searchBoardsByHits(int hits) {
        List<BoardEntity> boardEntityList = boardRepository.findByBoardHits(hits);
        return convertToDTOList(boardEntityList);
    }

    private List<BoardDTO> convertToDTOList(List<BoardEntity> boardEntityList) {
        List<BoardDTO> boardDTOList = new ArrayList<>();
        for (BoardEntity boardEntity : boardEntityList) {
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }


}