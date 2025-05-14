package com.ming.mgo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ming.mgo.dto.ResponseMessage;
import com.ming.mgo.dto.SgfDTO;
import com.ming.mgo.dto.SgfSaveRequest;
import com.ming.mgo.entity.Sgf;
import com.ming.mgo.service.SgfService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sgf")
@Validated
public class SgfController {

    @Autowired
    private SgfService sgfService;

    // 1. 列表查询（不含 sgf 字段）
    @GetMapping("/search")
    public ResponseEntity<Page<SgfDTO>> searchSgf(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<SgfDTO> result = sgfService.searchByPlayerName(keyword, page, size);
        return ResponseEntity.ok(result);
    }

    // 2. 详情获取（含 sgf 字段）
    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage<Sgf>> getSgfById(@PathVariable Long id) {
        return sgfService.getById(id)
                .map(sgf -> ResponseEntity.ok(ResponseMessage.success(sgf)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseMessage.error(400, "SGF not found")));
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseMessage<Long>> saveSgf(@RequestBody @Valid SgfSaveRequest request) {
        Sgf saved = sgfService.save(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseMessage.success(saved.getId()));
    }

    // @PostMapping("/debug")
    // public ResponseEntity<?> debug(@RequestBody Map<String, Object> map) {
    //     System.out.println(map);
    //     return ResponseEntity.ok(map);
    // }

}