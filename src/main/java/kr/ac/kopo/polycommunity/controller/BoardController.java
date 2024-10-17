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
import java.util.List;

@Controller
@RequiredArgsConstructor // final로 선언된 필드를 자동으로 초기화 -> 생성자를 만들어줌
@RequestMapping("/board") // /board를 대표적으로 묶어줌. 예를들면  밑에 /board/save를 해야되는데 여기서 /board정의해버려서 밑에는 save만 써주면됨
public class BoardController {
    private final BoardService boardService;  // ->public BoardController(BoardService boardService) {this.boardService = boardService;}이 되어 BoardController객체는 boardService 필드를 주입받아 사용할수 있게된다.
    private final CommentService commentService;

    @GetMapping("/save")
    public String saveForm(){
        return "save";
    }
    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) throws IOException { //@ModelAttribute를 이용해 BoardDTO객체 필드를 다가져옴
        System.out.println("boardDTO= " +boardDTO);
        boardService.save(boardDTO);
        return "redirect:/board/";
    }
    @GetMapping("/")
    public String findAll(Model model){ //Model 객체는 컨트롤러에서 뷰로 데이터를 전달하기 위해 사용
        //DB에서 전체 게시글 데이터를 가져와서 list.html에 보여준다.
        List<BoardDTO> boardDTOList =boardService.findAll();
        model.addAttribute("boardList", boardDTOList);
        return "list";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable Long id, Model model,
                           @RequestParam(value = "page", required = false, defaultValue = "1") int page){ //Model은 데이터를 담아가야하니깐 쓰는 객체
        /*
            해당 게시글의 조회수를 하나 올리고
            게시글 데이터를 가져와서 detail.html에 출력
        */
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        /*댓글 목록 가져오기*/
        List<CommentDTO> commentDTOList =commentService.findAll(id);
        model.addAttribute("commentList", commentDTOList);

        model.addAttribute("board", boardDTO);
        model.addAttribute("page", page);
        return "detail";
    }

    @GetMapping("/update/{id}")
    public  String updateForm(@PathVariable Long id, Model model){
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate", boardDTO);
        return  "update";

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

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        boardService.delete(id);
        return "redirect:/board/";
    }

    //board/paging?page=1
    @GetMapping("/paging")
    public String paging(@PageableDefault(page = 1) Pageable pageable, Model model){ //Pageable 인터페이스 Pageable ->org.springframework.data.domain. 이걸 import해줘야됨 java머시기는 안됨
        //pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
        int blockLimit = 3;
        int startPage =(((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit +1; // 1 4 7 10 요런 형태가 나옴
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        // page 갯수 20개
        // 현재 사용자가 3페이지에 있다면
        // 1 2 3
        // 현재 사용자가 7페이지에 있다면
        // 7 8 9
        // 보여지는 페이지 갯수 3개
        // 총 페이지 갯수 8개

        model.addAttribute("boardList", boardList);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        return "paging";




    }
}
