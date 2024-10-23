package kr.ac.kopo.polycommunity.controller;



import kr.ac.kopo.polycommunity.dto.BoardDTO;
import kr.ac.kopo.polycommunity.dto.CommentDTO;
import kr.ac.kopo.polycommunity.service.BoardService;
import kr.ac.kopo.polycommunity.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor // final로 선언된 필드를 자동으로 초기화 -> 생성자를 만들어줌
@RequestMapping("/board") // /board를 대표적으로 묶어줌. 예를들면  밑에 /board/save를 해야되는데 여기서 /board정의해버려서 밑에는 save만 써주면됨
public class BoardController {
    private final BoardService boardService;  // ->public BoardController(BoardService boardService) {this.boardService = boardService;}이 되어 BoardController객체는 boardService 필드를 주입받아 사용할수 있게된다.
    private final CommentService commentService;

    @GetMapping("/save")
    public String saveForm() {
        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException { //@ModelAttribute를 이용해 BoardDTO객체 필드를 다가져옴
        System.out.println("boardDTO= " + boardDTO);
        boardService.save(boardDTO);
        return "redirect:/board/";
    }

    // 기본 목록 페이지를 페이징 처리된 목록으로 변경
    @GetMapping("/")
    public String findAll(Model model, @PageableDefault(page = 1) Pageable pageable) {
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage = (((int) (Math.ceil((double) pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "list";
    }

    @GetMapping("/search")
    public String search(@RequestParam(value = "searchType") String searchType,
                         @RequestParam(value = "keyword") String keyword,
                         @PageableDefault(page = 1) Pageable pageable,  // 페이징 추가
                         Model model) {
        List<BoardDTO> searchResult;

        switch (searchType) {
            case "title":
                searchResult = boardService.searchBoardsByTitle(keyword);
                break;
            case "writer":
                searchResult = boardService.searchBoardsByWriter(keyword);
                break;
            case "hits":
                try {
                    searchResult = boardService.searchBoardsByHits(Integer.parseInt(keyword));
                } catch (NumberFormatException e) {
                    searchResult = new ArrayList<>();  // 유효하지 않은 숫자 입력 처리
                }
                break;
            default:
                searchResult = boardService.findAll();
        }

        // 검색 결과를 모델에 추가
        model.addAttribute("boardList", searchResult);

        // 페이징 관련 속성 추가
        int blockLimit = 3;
        int startPage = 1;
        int endPage = 1;

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("searchType", searchType);  // 검색 타입 유지
        model.addAttribute("keyword", keyword);        // 검색어 유지

        return "list";
    }

    // 페이징 처리된 목록 보기
    // 페이징 처리된 목록 보기
    @GetMapping("/list")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "list";
    }

    // 삭제 처리
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/list";
    }


    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @RequestParam(value = "page", required = false, defaultValue = "1") int page) { //Model은 데이터를 담아가야하니깐 쓰는 객체
        /*
            해당 게시글의 조회수를 하나 올리고
            게시글 데이터를 가져와서 detail.html에 출력
        */
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        /*댓글 목록 가져오기*/
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);


        model.addAttribute("board", boardDTO);
        model.addAttribute("page", page);
        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return "update";

    }

    @PostMapping("/update")
    public String update(@ModelAttribute BoardDTO boardDTO, @RequestParam(defaultValue = "1") int page, Model model) {
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        model.addAttribute("page", page);

        List<CommentDTO> commentDTOList = commentService.findAll(board.getId());
        model.addAttribute("commentList", commentDTOList);
        return "detail";
    }


    @GetMapping("/detail/{id}")
    public String findDetailById(@PathVariable Long id, Model model,
                                 @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        List<CommentDTO> commentDTOList = commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);
        model.addAttribute("board", boardDTO);
        model.addAttribute("page", page);
        return "detail";
    }

    //board/paging?page=1
//    @GetMapping("/paging")
//    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model) {
//        Page<BoardDTO> boardList = boardService.paging(pageable);
//        int blockLimit = 3;
//        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1;
//        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();
//
//        model.addAttribute("boardList", boardList);
//        model.addAttribute("startPage", startPage);
//        model.addAttribute("endPage", endPage);
//        model.addAttribute("currentPage", pageable.getPageNumber());
//        model.addAttribute("totalPages", boardList.getTotalPages());
//
//        return "list";  // paging.html 대신 list.html을 사용
//    }




    }
