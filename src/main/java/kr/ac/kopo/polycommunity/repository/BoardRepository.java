package kr.ac.kopo.polycommunity.repository;

import kr.ac.kopo.polycommunity.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<BoardEntity,Long> { //class아님 interface(인터페이스)임

    //update board_table set board_hits = board_hits+1 where id=?

    @Modifying
    @Query(value = "update BoardEntity b set b.boardHits = b.boardHits+1 where b.id=:id")
    void updateHits(@Param("id") Long id);

    // ID 내림차순으로 전체 게시글 조회
    List<BoardEntity> findAllByOrderByIdDesc();
}
