package kr.ac.kopo.polycommunity.repository;


import kr.ac.kopo.polycommunity.entity.CustomerBoardEntity;
import kr.ac.kopo.polycommunity.entity.CustomerCommentEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerSeviceCommentRepository extends JpaRepository<CustomerCommentEntity, Long> {

    //select * from comment_table where board_id=? order by id desc;
    List<CustomerCommentEntity> findAllBycustomerBoardEntityOrderByIdDesc(CustomerBoardEntity customerBoardEntity);


}
