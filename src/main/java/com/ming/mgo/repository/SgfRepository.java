package com.ming.mgo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ming.mgo.entity.Sgf;

public interface SgfRepository extends JpaRepository<Sgf, Long> {
    // 查询对局列表（不含sgf字段）——靠Spring Data JPA自动生成SQL
    // List<Sgf> findByBNameContainsOrWNameContains(String bName, String wName);

    @Query("SELECT s FROM Sgf s WHERE s.bName LIKE %:keyword% OR s.wName LIKE %:keyword%")
    Page<Sgf> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
